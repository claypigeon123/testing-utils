package com.purepigeon.test.utils.impl.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.ArtifactType;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.TypeRef;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.config.TestingUtilsAutoConfiguration;
import com.purepigeon.test.utils.test.ChildGenericTestData;
import com.purepigeon.test.utils.test.TestData;
import com.purepigeon.test.utils.test.TestDataCollection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithTestingUtils
@SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
class JacksonTestingUtilsTest {

    private static final String EXPECTED_SUITE = JacksonTestingUtilsTest.class.getSimpleName();

    private static final String TEST_DATA = "TestData.json";
    private static final String RENAMED_TEST_DATA = "RenamedTestData.json";

    @Autowired
    private TestingUtils testingUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void readInputObject_testCaseOnly(String testCase) {
        performReadTest(() -> testingUtils.readInputObject(testCase, TestData.class));
        performReadTest(() -> testingUtils.readInputObject(testCase, new TypeRef<>() {}));
    }

    @Test
    void readInputObject_testCaseAndArtifactName(String testCase) {
        performReadTest(() -> testingUtils.readInputObject(testCase, RENAMED_TEST_DATA, TestData.class));
        performReadTest(() -> testingUtils.readInputObject(testCase, RENAMED_TEST_DATA, new TypeRef<>() {}));
    }

    @Test
    void readExpectedObject_testCaseOnly(String testCase) {
        performReadTest(() -> testingUtils.readExpectedObject(testCase, TestData.class));
        performReadTest(() -> testingUtils.readExpectedObject(testCase, new TypeRef<>() {}));
    }

    @Test
    void readExpectedObject_testCaseAndArtifactName(String testCase) {
        performReadTest(() -> testingUtils.readExpectedObject(testCase, RENAMED_TEST_DATA, TestData.class));
        performReadTest(() -> testingUtils.readExpectedObject(testCase, RENAMED_TEST_DATA, new TypeRef<>() {}));
    }

    @ParameterizedTest
    @ValueSource(strings = { "INPUT", "EXPECTED" })
    void readObject(String artifactType, String testCase) {
        performReadTest(() -> testingUtils.readObject(testingUtils.getSuite(), testCase, ArtifactType.valueOf(artifactType), TEST_DATA, TestData.class));
        performReadTest(() -> testingUtils.readObject(testingUtils.getSuite(), testCase, ArtifactType.valueOf(artifactType), TEST_DATA, new TypeRef<>() {}));
    }

    @Test
    void jsonToObject() {
        performReadTest(() -> testingUtils.jsonToObject("{ \"id\": \"" + TestData.ID + "\", \"content\": \"" + TestData.CONTENT + "\" }", TestData.class));
        performReadTest(() -> testingUtils.jsonToObject("{ \"id\": \"" + TestData.ID + "\", \"content\": \"" + TestData.CONTENT + "\" }", new TypeRef<>() {}));
    }

    @Test
    void objectToJson() {
        performRawReadTest(() -> testingUtils.objectToJson(TestData.create()));
    }

    @Test
    void readInputString_testCaseOnly(String testCase) {
        performRawReadTest(() -> testingUtils.readInputString(testCase, TestData.class));
    }

    @Test
    void readInputString_testCaseAndArtifactName(String testCase) {
        performRawReadTest(() -> testingUtils.readInputString(testCase, RENAMED_TEST_DATA));
    }

    @Test
    void readExpectedString_testCaseOnly(String testCase) {
        performRawReadTest(() -> testingUtils.readExpectedString(testCase, TestData.class));
    }

    @Test
    void readExpectedString_testCaseAndArtifactName(String testCase) {
        performRawReadTest(() -> testingUtils.readExpectedString(testCase, RENAMED_TEST_DATA));
    }

    @ParameterizedTest
    @ValueSource(strings = { "INPUT", "EXPECTED" })
    void readString(String artifactType, String testCase) {
        performRawReadTest(() -> testingUtils.readString(testingUtils.getSuite(), testCase, ArtifactType.valueOf(artifactType), TEST_DATA));
    }

    @Test
    void assertObject_testCaseOnly(String testCase) {
        testingUtils.assertObject(testCase, TestData.create());
    }

    @Test
    void assertObject_testCaseAndArtifactName(String testCase) {
        testingUtils.assertObject(testCase, RENAMED_TEST_DATA, TestData.create());
    }

    @ParameterizedTest
    @CsvSource({
        "NON_EXTENSIBLE",
        "STRICT"
    })
    void assertObject_testCaseAndArtifactNameAndStrictness(String strictness, String testCase) {
        testingUtils.assertObject(testingUtils.getSuite(), testCase, TEST_DATA, TestData.create(), JSONCompareMode.valueOf(strictness));
    }

    @Test
    void getSuite() {
        assertEquals(EXPECTED_SUITE, testingUtils.getSuite());
    }

    @Test
    void getSuite_unset() {
        testingUtils.setSuite(null);
        assertThrows(IllegalStateException.class, () -> testingUtils.getSuite());
    }

    @Test
    void getTestCase(String testCase) {
        assertEquals("getTestCase", testCase);
    }

    @Test
    void artifactFileName() {
        assertEquals("String.json", testingUtils.artifactFileName(String.class));
        assertEquals("String.json", testingUtils.artifactFileName(new TypeRef<String>() {}));
        assertEquals("TestDataCollection.json", testingUtils.artifactFileName(new TypeRef<TestDataCollection<ChildGenericTestData>>() {}));
        assertEquals("BiFunction.json", testingUtils.artifactFileName(new TypeRef<BiFunction<String, String, String>>() {}));
    }

    @Test
    void genericReadTest(String testCase) {
        // when
        var data = testingUtils.readInputObject(testCase, new TypeRef<TestDataCollection<ChildGenericTestData>>() {});

        // then
        testingUtils.assertObject(testCase, data);
    }

    // --

    private void performReadTest(Supplier<TestData> resultSupplier) {
        // given
        var expected = TestData.create();

        // when
        var actual = resultSupplier.get();

        // then
        assertEquals(expected, actual);
    }

    @SneakyThrows
    private void performRawReadTest(Supplier<String> resultSupplier) {
        // given
        var expected = objectMapper.writeValueAsString(TestData.create());

        // when
        var actual = resultSupplier.get();

        // then
        JSONAssert.assertEquals(expected, actual, true);
    }
}
