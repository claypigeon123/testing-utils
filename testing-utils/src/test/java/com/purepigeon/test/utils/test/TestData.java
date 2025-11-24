package com.purepigeon.test.utils.test;

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

import lombok.Builder;

@Builder
//@Jacksonized pending lombok alignment
public record TestData(
    String id,
    String content
) {
    public static final String ID = "abe159f5-016d-4e91-b078-430605f0e776";
    public static final String CONTENT = "testing content";

    public static TestData create() {
        return TestData.builder()
            .id(ID)
            .content(CONTENT)
            .build();
    }
}
