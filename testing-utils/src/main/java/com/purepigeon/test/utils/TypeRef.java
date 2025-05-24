package com.purepigeon.test.utils;

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

import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 *     Testing utils' custom type reference to resolve types with generics.
 * </p>
 * <p>
 *     Sample usage:
 * </p>
 * <pre>
 * {@code
 *     // ...
 *     @SpringBootTest
 *     @WithTestingUtils
 *     class SampleTest {
 *
 *         @Autowired
 *         private TestingUtils testingUtils;
 *
 *         @Test
 *         void sampleTest(String testCase) {
 *             var inputList = testingUtils.readInputObject(testCase, new TypeRef<List<SampleData>>() {});
 *
 *             // ...
 *         }
 *     }
 * }
 * </pre>
 * @param <T> The referenced type
 */
public abstract class TypeRef<T> {

    @Getter
    protected final Type type;

    protected TypeRef() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("TypeRef has been constructed without actual type info");
        }

        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public String getSimpleName() {
        String[] tokens = type.getTypeName()
            .replaceAll("<.*>", "")
            .split("\\.");

        return tokens[tokens.length - 1];
    }
}
