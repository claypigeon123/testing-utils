package com.purepigeon.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.annotation.Suite;
import com.purepigeon.test.utils.annotation.TestCase;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.setup.TestApp;
import com.purepigeon.test.utils.setup.TestData;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Suite("utils")
@WithTestingUtils
@SpringBootTest(classes = TestApp.class)
class TestingUtilsTest {

    private static final String SUITE = "utils/" + TestingUtilsTest.class.getSimpleName();

    private static final String TEST_DATA = "TestData.json";
    private static final String RENAMED_TEST_DATA = "RenamedTestData.json";

    @Autowired
    private TestingUtils testingUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void readInputObject_testCaseOnly(String testCase) {
        performReadTest(() -> testingUtils.readInputObject(testCase, TestData.class));
    }

    @Test
    void readInputObject_suiteAndTestCase(String testCase) {
        performReadTest(() -> testingUtils.readInputObject(SUITE, testCase, TestData.class));
    }

    @Test
    void readInputObject_suiteAndTestCaseAndArtifactName(String testCase) {
        performReadTest(() -> testingUtils.readInputObject(SUITE, testCase, RENAMED_TEST_DATA, TestData.class));
    }

    @Test
    void readExpectedObject_testCaseOnly(String testCase) {
        performReadTest(() -> testingUtils.readExpectedObject(testCase, TestData.class));
    }

    @Test
    void readExpectedObject_suiteAndTestCase(String testCase) {
        performReadTest(() -> testingUtils.readExpectedObject(SUITE, testCase, TestData.class));
    }

    @Test
    void readExpectedObject_suiteAndTestCaseAndArtifactName(String testCase) {
        performReadTest(() -> testingUtils.readExpectedObject(SUITE, testCase, RENAMED_TEST_DATA, TestData.class));
    }

    @ParameterizedTest
    @ValueSource(strings = { "INPUT", "EXPECTED" })
    void readObject(String artifactType, String testCase) {
        performReadTest(() -> testingUtils.readObject(SUITE, testCase, TestingUtils.ArtifactType.valueOf(artifactType), TEST_DATA, TestData.class));
    }

    @Test
    void jsonToObject() {
        performReadTest(() -> testingUtils.jsonToObject("{ \"id\": \"" + TestData.ID + "\", \"content\": \"" + TestData.CONTENT + "\" }", TestData.class));
    }

    @Test
    void readInputString_testCaseOnly(String testCase) {
        performRawReadTest(() -> testingUtils.readInputString(testCase, TestData.class));
    }

    @Test
    void readInputString_suiteAndTestCase(String testCase) {
        performRawReadTest(() -> testingUtils.readInputString(SUITE, testCase, TestData.class));
    }

    @Test
    void readInputString_suiteAndTestCaseAndArtifactName(String testCase) {
        performRawReadTest(() -> testingUtils.readInputString(SUITE, testCase, RENAMED_TEST_DATA));
    }

    @Test
    void readExpectedString_testCaseOnly(String testCase) {
        performRawReadTest(() -> testingUtils.readExpectedString(testCase, TestData.class));
    }

    @Test
    void readExpectedString_suiteAndTestCase(String testCase) {
        performRawReadTest(() -> testingUtils.readExpectedString(SUITE, testCase, TestData.class));
    }

    @Test
    void readExpectedString_suiteAndTestCaseAndArtifactName(String testCase) {
        performRawReadTest(() -> testingUtils.readExpectedString(SUITE, testCase, RENAMED_TEST_DATA));
    }

    @ParameterizedTest
    @ValueSource(strings = { "INPUT", "EXPECTED" })
    void readString(String artifactType, String testCase) {
        performRawReadTest(() -> testingUtils.readString(SUITE, testCase, TestingUtils.ArtifactType.valueOf(artifactType), TEST_DATA));
    }

    @Test
    void assertObject_testCaseOnly(String testCase) {
        testingUtils.assertObject(testCase, TestData.create());
    }

    @Test
    void assertObject_suiteAndTestCase(String testCase) {
        testingUtils.assertObject(SUITE, testCase, TestData.create());
    }

    @Test
    void assertObject_suiteAndTestCaseAndArtifactName(String testCase) {
        testingUtils.assertObject(SUITE, testCase, RENAMED_TEST_DATA, TestData.create());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void assertObject_suiteAndTestCaseAndArtifactNameAndStrictness(boolean strict, String testCase) {
        testingUtils.assertObject(SUITE, testCase, TEST_DATA, TestData.create(), strict);
    }

    @Test
    void getSuite() {
        assertEquals(SUITE, testingUtils.getSuite());
    }

    @Test
    void getSuite_unset() {
        testingUtils.setSuite(null);
        assertThrows(IllegalStateException.class, () -> testingUtils.getSuite());
    }

    @Test
    void getTestCase_methodName(String testCase) {
        assertEquals("getTestCase_methodName", testCase);
    }

    @Test
    @TestCase("getTestCase_customName_123")
    void getTestCase_customName(String testCase) {
        assertEquals("getTestCase_customName_123", testCase);
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
