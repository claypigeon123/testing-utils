package com.purepigeon.test.utils.impl.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.ArtifactType;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.impl.AbstractTestingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.nio.file.Path;

/**
 * <p>
 *     Implementation of {@link TestingUtils} that uses the Jackson {@link ObjectMapper} for serialization and
 *     deserialization.
 * </p>
 */
@RequiredArgsConstructor
public class JacksonTestingUtils extends AbstractTestingUtils {

    @NonNull
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, Class<T> returnObjectType) {
        Path jsonPath = getArtifactPath(suite, testCase, artifactType, artifactName);
        return objectMapper.readValue(new File(jsonPath.toUri()), returnObjectType);
    }

    @Override
    @SneakyThrows
    public <T> T jsonToObject(String jsonContent, Class<T> returnObjectType) {
        return objectMapper.readValue(jsonContent, returnObjectType);
    }

    @Override
    @SneakyThrows
    public String objectToJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @Override
    @SneakyThrows
    public void assertObject(String suite, String testCase, String expectedArtifactName, Object actualObject, boolean strict) {
        String actualJson = objectMapper.writeValueAsString(actualObject);
        String expectedJson = readString(suite, testCase, ArtifactType.EXPECTED, expectedArtifactName);
        JSONAssert.assertEquals(expectedJson, actualJson, strict ? JSONCompareMode.NON_EXTENSIBLE : JSONCompareMode.LENIENT);
    }
}
