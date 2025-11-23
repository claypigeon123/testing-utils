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

import com.purepigeon.test.utils.TypeRef;
import tools.jackson.core.type.TypeReference;

import java.lang.reflect.Type;

/**
 * <p>
 *     Helper class to connect Testing utils' {@link TypeRef} to Jackson's {@link TypeReference}.
 * </p>
 * @param <T> The referenced type
 */
public class JacksonTypeRefConnector<T> extends TypeReference<T> {

    private final Type type;

    /**
     * <p>Construct a new instance using Testing Utils' own {@link TypeRef} class.</p>
     * @param ref The Testing Utils-provided type reference to use
     */
    public JacksonTypeRefConnector(TypeRef<T> ref) {
        this.type = ref.getType();
    }

    /**
     * <p>Getter for the underlying {@link Type}</p>
     * @return The underlying type this type reference connector refers to
     */
    @Override
    public Type getType() {
        return type;
    }
}
