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

import com.fasterxml.jackson.core.type.TypeReference;
import com.purepigeon.test.utils.TypeRef;

import java.lang.reflect.Type;

/**
 * <p>
 *     Helper class to connect Testing utils' {@link TypeRef} to Jackson's {@link TypeReference}.
 * </p>
 * @param <T> The referenced type
 */
public class JacksonTypeRefConnector<T> extends TypeReference<T> {

    private final Type type;

    public JacksonTypeRefConnector(TypeRef<T> ref) {
        this.type = ref.getType();
    }

    @Override
    public Type getType() {
        return type;
    }
}
