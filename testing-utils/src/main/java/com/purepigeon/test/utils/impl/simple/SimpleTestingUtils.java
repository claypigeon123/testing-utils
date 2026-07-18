package com.purepigeon.test.utils.impl.simple;

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
import org.jspecify.annotations.NullMarked;

/**
 * <p>
 *     Implementation of {@link TestingUtils} that uses no backing json library.
 * </p>
 * <p>
 *     Useful if you want to use Testing Utils just to load non POJO-mapped files (e.g. txt, csv, etc.).
 * </p>
 * <p>
 *     Throws {@link UnsupportedOperationException} when trying to use a method that includes mapping to / from a POJO.
 * </p>
 */
@NullMarked
public class SimpleTestingUtils extends AbstractTestingUtils {

    private static final String UNSUPPORTED_MESSAGE = "%s does not support methods that map to / from POJOs".formatted(SimpleTestingUtils.class.getSimpleName());

    @Override
    public <T> T readObject(String testCase, String artifactType, String artifactName, Class<T> returnObjectType) {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    @Override
    public <T> T readObject(String testCase, String artifactType, String artifactName, TypeRef<T> returnObjectType) {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    @Override
    public <T> T jsonToObject(String jsonContent, Class<T> returnObjectType) {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    @Override
    public <T> T jsonToObject(String jsonContent, TypeRef<T> returnObjectType) {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    @Override
    public String objectToJson(Object object) {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }
}
