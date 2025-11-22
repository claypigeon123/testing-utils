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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     Contains constants for default artifact types, used to build a path to a given test resource.
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class DefaultArtifactType {

    /**
     * <p>For "input" resources.</p>
     */
    public static final String INPUT = "input";

    /**
     * <p>For "expected" resources.</p>
     */
    public static final String EXPECTED = "expected";
}
