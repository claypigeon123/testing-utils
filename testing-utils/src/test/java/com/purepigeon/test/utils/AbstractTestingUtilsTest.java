package com.purepigeon.test.utils;

/*-
 * #%L
 * Testing Utils
 * %%
 * Copyright (C) 2025 Purepigeon
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.purepigeon.test.utils.annotation.Suite;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.test.ChildGenericTestData;
import com.purepigeon.test.utils.test.TestApp;
import com.purepigeon.test.utils.test.TestData;
import com.purepigeon.test.utils.test.TestDataCollection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithTestingUtils
@SpringBootTest(classes = TestApp.class)
@Suite(value = "TestingUtilsTest", appendClassName = false)
public abstract class AbstractTestingUtilsTest {

    private static final String TEST_DATA = "TestData.json";
    private static final String RENAMED_TEST_DATA = "RenamedTestData.json";

    protected TestingUtils testingUtils;
    protected abstract void setTestingUtils(TestingUtils testingUtils);

    public abstract void assertImpl();

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
    @EnumSource(ArtifactType.class)
    void readObject(ArtifactType artifactType, String testCase) {
        performReadTest(() -> testingUtils.readObject(testCase, artifactType, TEST_DATA, TestData.class));
        performReadTest(() -> testingUtils.readObject(testCase, artifactType, TEST_DATA, new TypeRef<>() {}));
    }

    @Test
    void jsonToObject() {
        String json = """
        {
            "id": "%s",
            "content": "%s"
        }
        """.formatted(TestData.ID, TestData.CONTENT);

        performReadTest(() -> testingUtils.jsonToObject(json, TestData.class));
        performReadTest(() -> testingUtils.jsonToObject(json, new TypeRef<>() {}));
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
    @EnumSource(ArtifactType.class)
    void readString(ArtifactType artifactType, String testCase) {
        performRawReadTest(() -> testingUtils.readString(testCase, artifactType, TEST_DATA));
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
        testingUtils.assertObject(testCase, TEST_DATA, TestData.create(), JSONCompareMode.valueOf(strictness));
    }

    @Test
    void getSuite() {
        assertEquals("TestingUtilsTest", testingUtils.getSuite());
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
        var expected = testingUtils.objectToJson(TestData.create());

        // when
        var actual = resultSupplier.get();

        // then
        JSONAssert.assertEquals(expected, actual, true);
    }
}
