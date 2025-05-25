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

import com.purepigeon.test.utils.test.TestApp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

class FixedClockTest {

    private static final String CUSTOM_DATE = "2010-06-10T17:00:00.123Z";

    @Nested
    @WithTestingUtils
    @SpringBootTest(classes = TestApp.class)
    class NoFixedClock {

        @Autowired(required = false)
        private Clock clock;

        @Test
        void testNoFixedClock() {
            assertNull(clock);
        }
    }

    @Nested
    @FixedClock
    @WithTestingUtils
    @SpringBootTest(classes = TestApp.class)
    class ClassLevel_DefaultDate {

        @Autowired
        private Clock clock;

        @Test
        void testFixedClock() {
            assertEquals(FixedClock.DEFAULT_INSTANT, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now(clock)));
        }
    }

    @Nested
    @FixedClock(CUSTOM_DATE)
    @WithTestingUtils
    @SpringBootTest(classes = TestApp.class)
    class ClassLevel_CustomDate {

        @Autowired
        private Clock clock;

        @Test
        void testFixedClock() {
            assertEquals(CUSTOM_DATE, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now(clock)));
        }
    }

    @Nested
    @FixedClock("1990-01-01T12:00:00.000Z")
    @WithTestingUtils
    @SpringBootTest(classes = TestApp.class)
    class MethodLevel_DefaultDate {

        @Autowired
        private Clock clock;

        @Test
        @FixedClock
        void testFixedClock() {
            assertEquals(FixedClock.DEFAULT_INSTANT, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now(clock)));
        }
    }

    @Nested
    @FixedClock("1990-01-01T12:00:00.000Z")
    @WithTestingUtils
    @SpringBootTest(classes = TestApp.class)
    class MethodLevel_CustomDate {

        @Autowired
        private Clock clock;

        @Test
        @FixedClock(CUSTOM_DATE)
        void testFixedClock() {
            assertEquals(CUSTOM_DATE, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now(clock)));
        }
    }

    @Nested
    class MethodLevel_WithNoClassLevel {

        @Test
        void testFixedClock() {
            EngineTestKit.engine(JupiterEngineDescriptor.ENGINE_ID)
                .selectors(selectMethod(InvalidUsageTest.class, "shouldFail"))
                .configurationParameter(JupiterConfiguration.DEACTIVATE_CONDITIONS_PATTERN_PROPERTY_NAME, "*")
                .execute()
                .testEvents()
                .assertThatEvents()
                .haveExactly(1, event(
                    finishedWithFailure(
                        instanceOf(IllegalStateException.class),
                        message("Also annotate your test class with @FixedClock - it cannot be used solely on the method level")
                    )
                ));
        }

        @Nested
        @Disabled("Excluded from automatic global test run")
        @WithTestingUtils
        @SpringBootTest(classes = TestApp.class)
        class InvalidUsageTest {

            @Test
            @FixedClock
            void shouldFail() {
                fail("This should not be executed");
            }
        }
    }
}
