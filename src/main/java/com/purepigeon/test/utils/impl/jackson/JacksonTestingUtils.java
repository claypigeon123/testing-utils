package com.purepigeon.test.utils.impl.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.ArtifactType;
import com.purepigeon.test.utils.TestingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.lang.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class JacksonTestingUtils implements TestingUtils {

    @NonNull
    private final ObjectMapper objectMapper;

    @Nullable
    private String suite;

    @SneakyThrows
    @Override
    public <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, Class<T> returnObjectType) {
        Path jsonPath = getArtifactPath(suite, testCase, artifactType, artifactName);
        return objectMapper.readValue(new File(jsonPath.toUri()), returnObjectType);
    }

    @SneakyThrows
    @Override
    public <T> T jsonToObject(String jsonContent, Class<T> returnObjectType) {
        return objectMapper.readValue(jsonContent, returnObjectType);
    }

    @SneakyThrows
    @Override
    public String objectToJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    @Override
    public String readString(String suite, String testCase, ArtifactType artifactType, String artifactName) {
        Path jsonPath = getArtifactPath(suite, testCase, artifactType, artifactName);
        return new String(Files.readAllBytes(jsonPath));
    }

    @SneakyThrows
    @Override
    public void assertObject(String suite, String testCase, String expectedArtifactName, Object actualObject, boolean strict) {
        String actualJson = objectMapper.writeValueAsString(actualObject);
        String expectedJson = readString(suite, testCase, ArtifactType.EXPECTED, expectedArtifactName);
        JSONAssert.assertEquals(expectedJson, actualJson, strict ? JSONCompareMode.NON_EXTENSIBLE : JSONCompareMode.LENIENT);
    }

    @Override
    public String getSuite() {
        if (suite == null || suite.isBlank()) {
            throw new IllegalStateException("Suite property is not set");
        }
        return suite;
    }

    @Override
    public void setSuite(@Nullable String suite) {
        this.suite = suite;
    }
}
