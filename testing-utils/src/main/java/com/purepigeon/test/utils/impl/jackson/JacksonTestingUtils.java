package com.purepigeon.test.utils.impl.jackson;

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

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.TypeRef;
import com.purepigeon.test.utils.impl.AbstractTestingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
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
    public <T> T readObject(String testCase, String artifactType, String artifactName, Class<T> returnObjectType) {
        Path jsonPath = getArtifactPath(getSuite(), testCase, artifactType, artifactName);
        return objectMapper.readValue(Files.readString(jsonPath), returnObjectType);
    }

    @Override
    @SneakyThrows
    public <T> T readObject(String testCase, String artifactType, String artifactName, TypeRef<T> returnObjectType) {
        Path jsonPath = getArtifactPath(getSuite(), testCase, artifactType, artifactName);
        return objectMapper.readValue(Files.readString(jsonPath), new JacksonTypeRefConnector<>(returnObjectType));
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
