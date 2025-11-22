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

/**
 * <p>
 *     Artifact type, used to build a path to a given test resource.
 * </p>
 * @deprecated This enum is being replaced in favor of strings, supported by default static constants
 *             in {@link DefaultArtifactType} for extensibility.
 *             Subject to removal in the next major version 2.0.0.
 * @see DefaultArtifactType
 */
@Deprecated(since = "1.4.0", forRemoval = true)
public enum ArtifactType {
    /**
     * <p>
     *     Means that the resource will be looked for in the input subfolder of the given suite and test case.
     * </p>
     */
    INPUT,

    /**
     * <p>
     *     Means that the resource will be looked for in the expected subfolder of the given suite and test case.
     * </p>
     */
    EXPECTED;

    /**
     * <p>
     *     Overridden to return the lowercase value of the enum constant.
     * </p>
     * @return the lowercase value of the enum constant.
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
