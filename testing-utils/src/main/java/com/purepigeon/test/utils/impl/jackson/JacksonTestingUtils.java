package com.purepigeon.test.utils.impl.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.ArtifactType;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.TypeRef;
import com.purepigeon.test.utils.impl.AbstractTestingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

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
    public <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, TypeRef<T> returnObjectType) {
        Path jsonPath = getArtifactPath(suite, testCase, artifactType, artifactName);
        return objectMapper.readValue(new File(jsonPath.toUri()), new JacksonTypeRefConnector<>(returnObjectType));
    }

    @Override
    @SneakyThrows
    public <T> T jsonToObject(String jsonContent, Class<T> returnObjectType) {
        return objectMapper.readValue(jsonContent, returnObjectType);
    }

    @Override
    @SneakyThrows
    public <T> T jsonToObject(String jsonContent, TypeRef<T> returnObjectType) {
        return objectMapper.readValue(jsonContent, new JacksonTypeRefConnector<>(returnObjectType));
    }

    @Override
    @SneakyThrows
    public String objectToJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }
}
