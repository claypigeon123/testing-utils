package com.purepigeon.test.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@TestComponent
@RequiredArgsConstructor
public class TestingUtils {
    @NonNull
    private final ObjectMapper objectMapper;

    @Nullable
    private String testSuite;

    public <T> T readInputObject(String testCase, Class<T> returnObjectType) {
        return readInputObject(getTestSuite(), testCase, returnObjectType);
    }

    public <T> T readExpectedObject(String testCase, Class<T> returnObjectType) {
        return readExpectedObject(getTestSuite(), testCase, returnObjectType);
    }

    public <T> T readInputObject(String testSuite, String testCase, Class<T> returnObjectType) {
        return readInputObject(testSuite, testCase, artefactFileName(returnObjectType), returnObjectType);
    }

    public <T> T readExpectedObject(String testSuite, String testCase, Class<T> returnObjectType) {
        return readExpectedObject(testSuite, testCase, artefactFileName(returnObjectType), returnObjectType);
    }

    public <T> T readInputObject(String testSuite, String testCase, String artefactName, Class<T> returnObjectType) {
        return readJsonToObject(testSuite, testCase, ArtefactType.INPUT, artefactName, returnObjectType);
    }

    public <T> T readExpectedObject(String testSuite, String testCase, String artefactName, Class<T> returnObjectType) {
        return readJsonToObject(testSuite, testCase, ArtefactType.EXPECTED, artefactName, returnObjectType);
    }

    @SneakyThrows
    public <T> T readJsonToObject(String testSuite, String testCase, ArtefactType artefactType, String artefactName,
                                  Class<T> returnObjectType) {
        Path jsonPath = getClassPathFilePath(testSuite, testCase, artefactType, artefactName);
        return objectMapper.readValue(new File(jsonPath.toUri()), returnObjectType);
    }

    @SneakyThrows
    public <T> T readJsonToObject(String jsonContent, Class<T> returnObjectType) {
        return objectMapper.readValue(jsonContent, returnObjectType);
    }

    @SneakyThrows
    public <T> List<T> readJsonToList(String testSuite, String testCase, ArtefactType artefactType, String artefactName) {
        Path jsonPath = getClassPathFilePath(testSuite, testCase, artefactType, artefactName);
        return objectMapper.readValue(new File(jsonPath.toUri()), new TypeReference<>() {});
    }

    public String readInputString(String testCase, Class<?> returnObjectType) {
        return readInputString(getTestSuite(), testCase, returnObjectType);
    }

    public String readExpectedString(String testCase, Class<?> returnObjectType) {
        return readExpectedString(getTestSuite(), testCase, returnObjectType);
    }

    public String readInputString(String testSuite, String testCase, Class<?> returnObjectType) {
        return readInputString(testSuite, testCase, artefactFileName(returnObjectType));
    }

    public String readExpectedString(String testSuite, String testCase, Class<?> returnObjectType) {
        return readExpectedString(testSuite, testCase, artefactFileName(returnObjectType));
    }

    public String readInputString(String testSuite, String testCase, String artefactName) {
        return readString(testSuite, testCase, ArtefactType.INPUT, artefactName);
    }

    public String readExpectedString(String testSuite, String testCase, String artefactName) {
        return readString(testSuite, testCase, ArtefactType.EXPECTED, artefactName);
    }

    @SneakyThrows
    public String readString(String testSuite, String testCase, ArtefactType artefactType, String artefactName) {
        Path jsonPath = getClassPathFilePath(testSuite, testCase, artefactType, artefactName);
        return new String(Files.readAllBytes(jsonPath));
    }

    public void assertObject(String testCase, Object actualObject) {
        assertObject(getTestSuite(), testCase, actualObject);
    }

    public void assertObject(String testSuite, String testCase, Object actualObject) {
        assertObject(testSuite, testCase, artefactFileName(actualObject.getClass()), actualObject, true);
    }

    @SneakyThrows
    public void assertObject(String testSuite, String testCase, String expectedArtefactName, Object actualObject, boolean strict) {
        String actualJson = objectMapper.writeValueAsString(actualObject);
        String expectedJson = readString(testSuite, testCase, ArtefactType.EXPECTED, expectedArtefactName);
        JSONAssert.assertEquals(expectedJson, actualJson, strict ? JSONCompareMode.NON_EXTENSIBLE : JSONCompareMode.LENIENT);
    }

    public void assertObject(String testSuite, String testCase, String expectedArtefactName, Object actualObject) {
        assertObject(testSuite, testCase, expectedArtefactName, actualObject, true);
    }

    private Path getClassPathFilePath(String testSuite, String testCase, ArtefactType artefactType, String artefactName) {
        Path classPathDirPath = getClassPathDirPath(testSuite, testCase, artefactType);
        return classPathDirPath.resolve(artefactName);
    }

    @SneakyThrows
    private Path getClassPathDirPath(String testSuite, String testCase, ArtefactType artefactType) {
        URL testSuiteResource = Objects.requireNonNull(getClass().getClassLoader().getResource(testSuite));
        Path testSuiteBasePath = Paths.get(testSuiteResource.toURI());

        Path testDirRelativePath = Paths.get(testCase, artefactType.toString());
        return testSuiteBasePath.resolve(testDirRelativePath);
    }

    private String artefactFileName(Class<?> returnObjectType) {
        return returnObjectType.getSimpleName() + ".json";
    }

    public String getTestSuite() {
        if (!StringUtils.hasText(testSuite)) {
            throw new UnsupportedOperationException("TestSuite property is not set");
        }
        return testSuite;
    }

    @Autowired(required = false)
    public void setTestSuite(@Nullable String testSuite) {
        this.testSuite = testSuite;
    }

    public enum ArtefactType {
        INPUT, EXPECTED;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
