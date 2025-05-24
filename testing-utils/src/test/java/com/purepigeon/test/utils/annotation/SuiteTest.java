package com.purepigeon.test.utils.annotation;

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
import com.purepigeon.test.utils.config.TestingUtilsAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithTestingUtils
@Suite("annotation")
@SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
class SuiteTest {

    private static final String EXPECTED_SUITE = "annotation/" + SuiteTest.class.getSimpleName();

    @Autowired
    private TestingUtils testingUtils;

    @Test
    void getSuite() {
        assertEquals(EXPECTED_SUITE, testingUtils.getSuite());
    }
}
