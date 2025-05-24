package com.purepigeon.test.utils.impl.jsonb;

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
