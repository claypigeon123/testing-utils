package com.purepigeon.test.utils.impl.jsonb;

import com.purepigeon.test.utils.ArtifactType;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.TypeRef;
import com.purepigeon.test.utils.impl.AbstractTestingUtils;
import jakarta.json.bind.Jsonb;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p>
 *     Implementation of {@link TestingUtils} that uses the Jakarta {@link Jsonb} for serialization and
 *     deserialization.
 * </p>
 */
@RequiredArgsConstructor
public class JsonbTestingUtils extends AbstractTestingUtils {

    @NonNull
    private final Jsonb jsonb;

    @Override
    @SneakyThrows
    public <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, Class<T> returnObjectType) {
        Path jsonPath = getArtifactPath(suite, testCase, artifactType, artifactName);
        return jsonb.fromJson(Files.readString(jsonPath), returnObjectType);
    }

    @Override
    @SneakyThrows
    public <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, TypeRef<T> returnObjectType) {
        Path jsonPath = getArtifactPath(suite, testCase, artifactType, artifactName);
        return jsonb.fromJson(Files.readString(jsonPath), returnObjectType.getType());
    }

    @Override
    public <T> T jsonToObject(String jsonContent, Class<T> returnObjectType) {
        return jsonb.fromJson(jsonContent, returnObjectType);
    }

    @Override
    public <T> T jsonToObject(String jsonContent, TypeRef<T> returnObjectType) {
        return jsonb.fromJson(jsonContent, returnObjectType.getType());
    }

    @Override
    public String objectToJson(Object object) {
        return jsonb.toJson(object);
    }
}
