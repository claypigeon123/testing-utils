package com.purepigeon.test.utils.impl.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purepigeon.test.utils.ArtifactType;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.TypeRef;
import com.purepigeon.test.utils.impl.AbstractTestingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p>
 *     Implementation of {@link TestingUtils} that uses Google {@link Gson} for serialization and
 *     deserialization.
 * </p>
 */
@RequiredArgsConstructor
public class GsonTestingUtils extends AbstractTestingUtils {

    @NonNull
    private final Gson gson;

    @Override
    @SneakyThrows
    public <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, Class<T> returnObjectType) {
        Path jsonPath = getArtifactPath(suite, testCase, artifactType, artifactName);
        return gson.fromJson(Files.readString(jsonPath), returnObjectType);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, TypeRef<T> returnObjectType) {
        Path jsonPath = getArtifactPath(suite, testCase, artifactType, artifactName);
        return gson.fromJson(Files.readString(jsonPath), (TypeToken<T>) TypeToken.get(returnObjectType.getType()));
    }

    @Override
    @SneakyThrows
    public <T> T jsonToObject(String jsonContent, Class<T> returnObjectType) {
        return gson.fromJson(jsonContent, returnObjectType);
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T jsonToObject(String jsonContent, TypeRef<T> returnObjectType) {
        return gson.fromJson(jsonContent, (TypeToken<T>) TypeToken.get(returnObjectType.getType()));
    }

    @Override
    @SneakyThrows
    public String objectToJson(Object object) {
        return gson.toJson(object);
    }
}
