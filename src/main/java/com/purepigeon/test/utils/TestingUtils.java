package com.purepigeon.test.utils;

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
        return readInputObject(testSuite, testCase, artifactFileName(returnObjectType), returnObjectType);
    }

    public <T> T readExpectedObject(String testSuite, String testCase, Class<T> returnObjectType) {
        return readExpectedObject(testSuite, testCase, artifactFileName(returnObjectType), returnObjectType);
    }

    public <T> T readInputObject(String testSuite, String testCase, String artifactName, Class<T> returnObjectType) {
        return readObject(testSuite, testCase, ArtifactType.INPUT, artifactName, returnObjectType);
    }

    public <T> T readExpectedObject(String testSuite, String testCase, String artifactName, Class<T> returnObjectType) {
        return readObject(testSuite, testCase, ArtifactType.EXPECTED, artifactName, returnObjectType);
    }

    @SneakyThrows
    public <T> T readObject(String testSuite, String testCase, ArtifactType artifactType, String artifactName, Class<T> returnObjectType) {
        Path jsonPath = getClassPathFilePath(testSuite, testCase, artifactType, artifactName);
        return objectMapper.readValue(new File(jsonPath.toUri()), returnObjectType);
    }

    @SneakyThrows
    public <T> T jsonToObject(String jsonContent, Class<T> returnObjectType) {
        return objectMapper.readValue(jsonContent, returnObjectType);
    }

    public String readInputString(String testCase, Class<?> returnObjectType) {
        return readInputString(getTestSuite(), testCase, returnObjectType);
    }

    public String readExpectedString(String testCase, Class<?> returnObjectType) {
        return readExpectedString(getTestSuite(), testCase, returnObjectType);
    }

    public String readInputString(String testSuite, String testCase, Class<?> returnObjectType) {
        return readInputString(testSuite, testCase, artifactFileName(returnObjectType));
    }

    public String readExpectedString(String testSuite, String testCase, Class<?> returnObjectType) {
        return readExpectedString(testSuite, testCase, artifactFileName(returnObjectType));
    }

    public String readInputString(String testSuite, String testCase, String artifactName) {
        return readString(testSuite, testCase, ArtifactType.INPUT, artifactName);
    }

    public String readExpectedString(String testSuite, String testCase, String artifactName) {
        return readString(testSuite, testCase, ArtifactType.EXPECTED, artifactName);
    }

    @SneakyThrows
    public String readString(String testSuite, String testCase, ArtifactType artifactType, String artifactName) {
        Path jsonPath = getClassPathFilePath(testSuite, testCase, artifactType, artifactName);
        return new String(Files.readAllBytes(jsonPath));
    }

    public void assertObject(String testCase, Object actualObject) {
        assertObject(getTestSuite(), testCase, actualObject);
    }

    public void assertObject(String testSuite, String testCase, Object actualObject) {
        assertObject(testSuite, testCase, artifactFileName(actualObject.getClass()), actualObject, true);
    }

    public void assertObject(String testSuite, String testCase, String expectedArtefactName, Object actualObject) {
        assertObject(testSuite, testCase, expectedArtefactName, actualObject, true);
    }

    @SneakyThrows
    public void assertObject(String testSuite, String testCase, String expectedArtefactName, Object actualObject, boolean strict) {
        String actualJson = objectMapper.writeValueAsString(actualObject);
        String expectedJson = readString(testSuite, testCase, ArtifactType.EXPECTED, expectedArtefactName);
        JSONAssert.assertEquals(expectedJson, actualJson, strict ? JSONCompareMode.NON_EXTENSIBLE : JSONCompareMode.LENIENT);
    }

    private Path getClassPathFilePath(String testSuite, String testCase, ArtifactType artifactType, String artifactName) {
        Path classPathDirPath = getClassPathDirPath(testSuite, testCase, artifactType);
        return classPathDirPath.resolve(artifactName);
    }

    @SneakyThrows
    private Path getClassPathDirPath(String testSuite, String testCase, ArtifactType artifactType) {
        URL testSuiteResource = Objects.requireNonNull(getClass().getClassLoader().getResource(testSuite));
        Path testSuiteBasePath = Paths.get(testSuiteResource.toURI());

        Path testDirRelativePath = Paths.get(testCase, artifactType.toString());
        return testSuiteBasePath.resolve(testDirRelativePath);
    }

    private String artifactFileName(Class<?> returnObjectType) {
        return returnObjectType.getSimpleName() + ".json";
    }

    public String getTestSuite() {
        if (!StringUtils.hasText(testSuite)) {
            throw new IllegalStateException("TestSuite property is not set");
        }
        return testSuite;
    }

    @Autowired(required = false)
    public void setTestSuite(@Nullable String testSuite) {
        this.testSuite = testSuite;
    }

    public enum ArtifactType {
        INPUT, EXPECTED;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
