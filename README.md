

# Testing Utilities for Spring

Streamline unit testing with test resources, let this component take care of the boilerplate.

## Usage

### Add Maven test dependency
```xml
<dependency>
    <groupId>com.purepigeon.test</groupId>
    <artifactId>testing-utils</artifactId>
    <version>0.2.1</version>
    <scope>test</scope>
</dependency>
```

### Description

#### Rationale
Unit tests are often polluted with many lines of preparing objects as inputs / expectations.
The `TestingUtils` class provides various utilities for loading test resources (and asserting on results),
allowing the boilerplate steps to move from the unit test code to the classpath test resources. This keeps the
unit test focused on the testing itself.

#### The `TestingUtils` class
This class is the main workhorse of this component. It - for one - streamlines the reading of test
resources into objects or raw strings.

To do this, it keeps track of the current test suite via its `suite` property. The path to a test resource is
then constructed as such:

`classpath:/{suite}/{testCase}/{artifactType}/{artifactName}`

Where:
  - `suite` is a given per test class (valued by default as the test class name)
  - `testCase` is a given per test method (valued by default as the test method name)
  - `artifactType` depends on the desired resource type ("input" or "expected")
  - `artifactName` depends on the desired artifact

As an example with this in mind, within the `sampleTest` method of a `SampleServiceTest` class, creating an
instance of an input object can simply become:
```java
SampleRequest input = testingUtils.readInputObject(testCase, SampleRequest.class);
```
Where `suite` is already known and `artifactType` is implied by the method we use; so we only need
to specify `testCase` and `artifactName`. In this case, `artifactName` is inferred from the type to be
`SampleRequest.json`, so the path that the resource is read from becomes:

`classpath:/SampleServiceTest/sampleTest/input/SampleRequest.json`

#### How it works

Using the `@WithTestingUtils` annotation on a test class has the following effects:
  - Registers a `TestingUtils` bean in the test application context
  - Enables the Junit 5 extension `TestingUtilsExtension`, which:
    - Sets the aforementioned `suite` property in the `TestingUtils` instance
    - Resolves a `testCase` string argument for test methods

#### The `@Suite` annotation
When a test class is annotated with this, the annotation value becomes a prefix for the `suite` property. This
allows for more fine-grained / logical organization of the test resources.

Keeping with the above example, if the test class `SampleServiceTest` is also annotated with:
```java
@Suite("service/impl")
```
Then the path to the resource in the same test method would become:

`classpath:/service/impl/SampleServiceTest/sampleTest/input/SampleRequest.json`

#### The `@TestCase` annotation
When a test method is annotated with this, it overrides the resolved value for the `testCase` method argument (which
by default would be the test method name). This is useful if we want to reuse the same resources
across multiple test methods.

Keeping with the above example, if the test method is also annotated with:
```java
@TestCase("sampleTest_successful")
```
Then the path to the resource would become:

`classpath:/service/impl/SampleServiceTest/sampleTest_successful/input/SampleRequest.json`

### Quickstart

#### Example controller unit testing

```java
// ...
@WithTestingUtils
@Suite("controller")
@WebMvcTest(controllers = SampleController.class)
class SampleControllerTest {

    @Autowired
    private TestingUtils testingUtils;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SampleService sampleService;

    // ...

    // testCase argument is resolved to value of @TestCase annotation
    @Test
    @TestCase("processReport")
    void processReport_200(String testCase) {
        // load input test resource as raw json string located at
        // classpath:/controller/SampleControllerTest/processReport/input/ProcessReportRequest.json
        String request = testingUtils.readInputString(testCase, ProcessReportRequest.class);
        
        // load expected test resource as raw json string located at
        // classpath:/controller/SampleControllerTest/processReport/expected/ProcessReportResponse.json
        String response = testingUtils.readExpectedString(testCase, ProcessReportResponse.class);

        when(sampleService.processReport(any()))
            .thenReturn(testingUtils.jsonToObject(response, ProcessReportResponse.class));

        mockMvc.perform(
            post("/reports/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isOk())
            .andExpect(content().json(response));
        
        verify(sampleService).processReport(request);
    }
}
```

#### Example service unit testing

```java
// ...
@SpringBootTest
@Suite("service")
@WithTestingUtils
class SampleServiceTest {

    @Autowired
    private TestingUtils testingUtils;

    @Autowired
    private SampleService sampleService;
    
    // ...

    // testCase argument is resolved to test method name
    @Test
    void processReport_successful(String testCase) {
        // load test resource as ProcessReportRequest object located at
        // classpath:/service/SampleServiceTest/processReport_successful/input/ProcessReportRequest.json
        ProcessReportRequest input = testingUtils.readInputObject(testCase, ProcessReportRequest.class);

        // mocking...

        ProcessReportResponse result = sampleService.processReport(input);

        // JSON assertion against test resource located at
        // /service/SampleServiceTest/processReport_successful/expected/ProcessReportResponse.json
        testingUtils.assertObject(testCase, result);
        
        // verifications...
    }
    
    // ...
}
```