# Testing Utilities for Spring
Streamline Spring Boot unit and integration tests by eliminating boilerplate code related to test resource
loading and assertion. This library simplifies the process of managing test data, allowing developers to focus on
the core logic of the tests.

# Key features

- **Automated Test Resource Loading:**  Automatically loads test resources (JSON, XML, text files, etc.) based
  on a convention-driven approach.
- **Type Conversion:** Converts test resource content into Java objects seamlessly.
- **JSON Assertions:** Simplifies the comparison of actual results with expected JSON payloads.
- **JUnit 5 Integration:**  Provides a JUnit 5 extension for easy integration into your existing test suites.
- **Suite and TestCase Organization:** Organizes test resources logically using `@Suite` and `@TestCase` annotations.
- **Reduces Boilerplate:**  Minimizes repetitive code for reading input data and verifying expected outputs.

# Why use this library?

Unit tests often become cluttered with code that prepares input objects and defines expected results.
This library addresses this problem by:

- **Centralizing Resource Loading:**  Handles the complexities of locating and reading test resources.
- **Improving Readability:** Makes your tests more concise and easier to understand by removing boilerplate.
- **Enhancing Maintainability:**  Simplifies test maintenance by separating test data specifics from test logic.
- **Promoting Consistency:** Enforces a consistent approach to test resource management across your project.

# Usage

## 1. Add dependency
Maven:
```xml
<dependency>
    <groupId>com.purepigeon.test</groupId>
    <artifactId>testing-utils</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

Gradle:
```groovy
testImplementation 'com.purepigeon.test:testing-utils:1.0.0'
```

## 2. Annotate test class
Add the `@WithTestingUtils` annotation to your test class. This registers a `TestingUtils` bean in the test application
context and enables the JUnit 5 extension:

```java
@SpringBootTest
@WithTestingUtils
public class MyServiceTest {

    @Autowired
    private TestingUtils testingUtils;

    // ...
}
```

## 3. Understand resource loading convention
The `TestingUtils` class uses a convention to locate test resources based on the following path structure:

```text
classpath:/{suite}/{testCase}/{artifactType}/{artifactName}
```

Let's break down each part of this path:
- **suite**: Represents a logical grouping of tests, typically related to a specific class or component.
  By default, this is the name of the test class. It can be customized using the `@Suite` annotation.
- **testCase**: Identifies a specific test method within the suite. By default, this is the name of the test method, and
  is provided as a `String testCase` argument to the test method. It can be overridden using the `@TestCase` annotation.
- **artifactType**: Indicates the type of test resource ("input" or "expected"). Most `TestingUtils` methods
  determine this value based on the method name (e.g., `readInputObject` implies "input").
- **artifactName**: The name of the test resource file. This can be inferred from the class type
  provided to the `TestingUtils` method (e.g., "SampleRequest.json" for
  `readInputObject(testCase, SampleRequest.class)`), or provided directly in other respective methods.

## 4. Annotations

### `@WithTestingUtils`
- Applied to a test class.
- Registers a `TestingUtils` bean in the test application context.
- Enables the `TestingUtilsExtension` JUnit 5 extension.

### `@Suite`
- Applied to a test class. 
- Specifies a prefix for the `suite` property in the `TestingUtils` instance. 
- Allows for logical organization of test resources.

```java
@SpringBootTest
@WithTestingUtils
@Suite("service/impl")
public class SampleServiceTest {
    // ...
}
```
In this example, the `suite` path will start with `service/impl`, the full path being: `service/impl/SampleServiceTest`

### `@TestCase`
- Applied to a test method. 
- Overrides the default testCase argument value (which is the test method name). 
- Useful for reusing test resources across multiple test methods.

```java
@SpringBootTest
@WithTestingUtils
public class SampleServiceTest {

    @Autowired
    private TestingUtils testingUtils;

    @Test
    @TestCase("processReport_v2")
    void processReport_successful(String testCase) {
        // ...
    }
}
```
In this example, the value of the `testCase` argument will be `processReport_v2`.

## 5. Using the `TestingUtils` bean
The TestingUtils bean provides several methods for loading and asserting on test resources, some of which are:
- **readInputObject(String testCase, Class<T> type)**: Reads an input resource and converts it into an object of
  the specified type. 
- **readExpectedObject(String testCase, Class<T> type)**: Reads an expected resource and converts it into an object
  of the specified type. 
- **readInputString(String testCase, Class<?> type)**: Reads an input resource as a raw string.
- **readExpectedString(String testCase, Class<?> type)**: Reads an expected resource as a raw string. 
- **assertObject(String testCase, Object actual)**: Asserts that an object matches the expected JSON resource. 
- **jsonToObject(String json, Class<T> type)**: Converts a JSON string to an object.
- **objectToJson(String json, Class<T> type)**: Converts an object to a JSON string.

# Example Usage
## Controller
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
    
    @Test
    @TestCase("processReport")
    void processReport_200(String testCase) {
        // given
        String request = testingUtils.readInputString(testCase, ProcessReportRequest.class);
        String response = testingUtils.readExpectedString(testCase, ProcessReportResponse.class);

        when(sampleService.processReport(any()))
            .thenReturn(testingUtils.jsonToObject(response, ProcessReportResponse.class));

        // expect
        mockMvc.perform(
            post("/reports/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isOk())
            .andExpect(content().json(response));
        
        verify(sampleService).processReport(testingUtils.jsonToObject(request, ProcessReportRequest.class));
    }
}
```

## Service
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
    
    @Test
    void processReport_successful(String testCase) {
        // given
        ProcessReportRequest input = testingUtils.readInputObject(testCase, ProcessReportRequest.class);

        // mocking...

        // when
        ProcessReportResponse result = sampleService.processReport(input);

        // then
        testingUtils.assertObject(testCase, result);
        
        // verifications...
    }
    
    // ...
}
```

# Contributing
Contributions are welcome! If you have ideas for new features or improvements, please submit a pull request or
open an issue to discuss further.

# License
This project is licensed under Apache 2.0. See the LICENSE file for details.

# Troubleshooting
- **Resource not found**: Double-check the resource path and ensure that the file exists in the correct
  location under the `src/test/resources` directory. Pay close attention to the suite, testCase, artifactType, and
  artifactName components of the path.
- **JSON / type conversion errors**: Verify that your JSON files are valid and well-formed, or that your potentially customized
  ObjectMapper configuration is present in the test application context.
