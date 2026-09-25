package com.purepigeon.test.utils;

/*-
 * #%L
 * Testing Utils
 * %%
 * Copyright (C) 2025 - 2026 Purepigeon
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

import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.jackson2.Jackson2TestingUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithTestingUtils
@SpringBootTest(classes = TestApp.class)
class Jackson2AutoConfigurationTest {

    @Autowired
    private TestingUtils testingUtils;

    @Test
    void assertTestingUtilsImpl() {
        assertEquals(Jackson2TestingUtils.class, testingUtils.getClass());
    }
}
