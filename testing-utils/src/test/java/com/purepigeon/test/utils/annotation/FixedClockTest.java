package com.purepigeon.test.utils.annotation;

import com.purepigeon.test.utils.config.TestingUtilsAutoConfiguration;
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
    @SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
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
    @SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
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
    @SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
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
    @SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
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
    @SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
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
        @SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
        class InvalidUsageTest {

            @Test
            @FixedClock
            void shouldFail() {
                fail("This should not be executed");
            }
        }
    }
}
