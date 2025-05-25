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
import com.purepigeon.test.utils.test.TestApp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

class SuiteTest {

    @Nested
    @WithTestingUtils
    @SpringBootTest(classes = TestApp.class)
    class NoAnnotationTest {

        @Autowired
        private TestingUtils testingUtils;

        @Test
        void getSuite() {
            assertEquals("NoAnnotationTest", testingUtils.getSuite());
        }
    }

    @Nested
    @WithTestingUtils
    @Suite("annotation")
    @SpringBootTest(classes = TestApp.class)
    class ValueTest {

        @Autowired
        private TestingUtils testingUtils;

        @Test
        void getSuite() {
            assertEquals("annotation/ValueTest", testingUtils.getSuite());
        }
    }

    @Nested
    @WithTestingUtils
    @Suite(value = "annotation", appendClassName = false)
    @SpringBootTest(classes = TestApp.class)
    class NoAppendClassNameTest {

        @Autowired
        private TestingUtils testingUtils;

        @Test
        void getSuite() {
            assertEquals("annotation", testingUtils.getSuite());
        }
    }

    @Nested
    class BlankSuiteValueTest {

        @Test
        void getSuite() {
            EngineTestKit.engine(JupiterEngineDescriptor.ENGINE_ID)
                .selectors(selectMethod(SuiteTest.BlankSuiteValueTest.InvalidUsageTest.class, "shouldFail"))
                .configurationParameter(JupiterConfiguration.DEACTIVATE_CONDITIONS_PATTERN_PROPERTY_NAME, "*")
                .execute()
                .testEvents()
                .assertThatEvents()
                .haveExactly(1, event(
                    finishedWithFailure(
                        instanceOf(IllegalStateException.class),
                        message("@Suite value cannot be blank - please provide a non-blank value or remove the annotation completely to use the default suite name (InvalidUsageTest)")
                    )
                ));
        }

        @Nested
        @WithTestingUtils
        @Suite("")
        @Disabled("Excluded from automatic global test run")
        @SpringBootTest(classes = TestApp.class)
        class InvalidUsageTest {

            @Test
            void shouldFail() {
                fail("This should not be executed");
            }
        }
    }
}
