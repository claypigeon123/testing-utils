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
    private String suite;

    public <T> T readInputObject(String testCase, Class<T> returnObjectType) {
        return readInputObject(getSuite(), testCase, returnObjectType);
    }

    public <T> T readExpectedObject(String testCase, Class<T> returnObjectType) {
        return readExpectedObject(getSuite(), testCase, returnObjectType);
    }

    public <T> T readInputObject(String suite, String testCase, Class<T> returnObjectType) {
        return readInputObject(suite, testCase, artifactFileName(returnObjectType), returnObjectType);
    }

    public <T> T readExpectedObject(String suite, String testCase, Class<T> returnObjectType) {
        return readExpectedObject(suite, testCase, artifactFileName(returnObjectType), returnObjectType);
    }

    public <T> T readInputObject(String suite, String testCase, String artifactName, Class<T> returnObjectType) {
        return readObject(suite, testCase, ArtifactType.INPUT, artifactName, returnObjectType);
    }

    public <T> T readExpectedObject(String suite, String testCase, String artifactName, Class<T> returnObjectType) {
        return readObject(suite, testCase, ArtifactType.EXPECTED, artifactName, returnObjectType);
    }

    @SneakyThrows
    public <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, Class<T> returnObjectType) {
        Path jsonPath = getClassPathFilePath(suite, testCase, artifactType, artifactName);
        return objectMapper.readValue(new File(jsonPath.toUri()), returnObjectType);
    }

    @SneakyThrows
    public <T> T jsonToObject(String jsonContent, Class<T> returnObjectType) {
        return objectMapper.readValue(jsonContent, returnObjectType);
    }

    public String readInputString(String testCase, Class<?> returnObjectType) {
        return readInputString(getSuite(), testCase, returnObjectType);
    }

    public String readExpectedString(String testCase, Class<?> returnObjectType) {
        return readExpectedString(getSuite(), testCase, returnObjectType);
    }

    public String readInputString(String suite, String testCase, Class<?> returnObjectType) {
        return readInputString(suite, testCase, artifactFileName(returnObjectType));
    }

    public String readExpectedString(String suite, String testCase, Class<?> returnObjectType) {
        return readExpectedString(suite, testCase, artifactFileName(returnObjectType));
    }

    public String readInputString(String suite, String testCase, String artifactName) {
        return readString(suite, testCase, ArtifactType.INPUT, artifactName);
    }

    public String readExpectedString(String suite, String testCase, String artifactName) {
        return readString(suite, testCase, ArtifactType.EXPECTED, artifactName);
    }

    @SneakyThrows
    public String readString(String suite, String testCase, ArtifactType artifactType, String artifactName) {
        Path jsonPath = getClassPathFilePath(suite, testCase, artifactType, artifactName);
        return new String(Files.readAllBytes(jsonPath));
    }

    public void assertObject(String testCase, Object actualObject) {
        assertObject(getSuite(), testCase, actualObject);
    }

    public void assertObject(String suite, String testCase, Object actualObject) {
        assertObject(suite, testCase, artifactFileName(actualObject.getClass()), actualObject, true);
    }

    public void assertObject(String suite, String testCase, String expectedArtefactName, Object actualObject) {
        assertObject(suite, testCase, expectedArtefactName, actualObject, true);
    }

    @SneakyThrows
    public void assertObject(String suite, String testCase, String expectedArtefactName, Object actualObject, boolean strict) {
        String actualJson = objectMapper.writeValueAsString(actualObject);
        String expectedJson = readString(suite, testCase, ArtifactType.EXPECTED, expectedArtefactName);
        JSONAssert.assertEquals(expectedJson, actualJson, strict ? JSONCompareMode.NON_EXTENSIBLE : JSONCompareMode.LENIENT);
    }

    private Path getClassPathFilePath(String suite, String testCase, ArtifactType artifactType, String artifactName) {
        Path classPathDirPath = getClassPathDirPath(suite, testCase, artifactType);
        return classPathDirPath.resolve(artifactName);
    }

    @SneakyThrows
    private Path getClassPathDirPath(String suite, String testCase, ArtifactType artifactType) {
        URL suiteResource = Objects.requireNonNull(getClass().getClassLoader().getResource(suite));
        Path suiteBasePath = Paths.get(suiteResource.toURI());

        Path testDirRelativePath = Paths.get(testCase, artifactType.toString());
        return suiteBasePath.resolve(testDirRelativePath);
    }

    private String artifactFileName(Class<?> returnObjectType) {
        return returnObjectType.getSimpleName() + ".json";
    }

    public String getSuite() {
        if (!StringUtils.hasText(suite)) {
            throw new IllegalStateException("Suite property is not set");
        }
        return suite;
    }

    @Autowired(required = false)
    public void setSuite(@Nullable String suite) {
        this.suite = suite;
    }

    public enum ArtifactType {
        INPUT, EXPECTED;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
