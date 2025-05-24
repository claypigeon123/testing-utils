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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.annotation.Suite;
import com.purepigeon.test.utils.annotation.TestCase;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Suite("plain")
@WithTestingUtils
class PlainUsageTest {

    private static final String EXPECTED_SUITE = "plain/%s".formatted(PlainUsageTest.class.getSimpleName());

    private final TestingUtils testingUtils = new JacksonTestingUtils(new ObjectMapper());

    @Test
    void defaultTestCaseTest(String testCase) {
        assertEquals(EXPECTED_SUITE, testingUtils.getSuite());
        assertEquals("defaultTestCaseTest", testCase);
    }

    @Test
    @TestCase("overrideTestCaseTest_123")
    void overrideTestCaseTest(String testCase) {
        assertEquals(EXPECTED_SUITE, testingUtils.getSuite());
        assertEquals("overrideTestCaseTest_123", testCase);
    }
}
