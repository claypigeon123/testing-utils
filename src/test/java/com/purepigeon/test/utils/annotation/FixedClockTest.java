package com.purepigeon.test.utils.annotation;

import com.purepigeon.test.utils.config.TestingUtilsAutoConfiguration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixedClockTest {

    private static final String CUSTOM_DATE = "2010-06-10T17:00:00.123Z";

    @Nested
    @FixedClock
    @WithTestingUtils
    @SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
    class DefaultDate {

        @Autowired
        private Clock clock;

        @Test
        void testFixedClock() {
            assertEquals(FixedClock.DEFAULT_TIME, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now(clock)));
        }
    }

    @Nested
    @FixedClock(CUSTOM_DATE)
    @WithTestingUtils
    @SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
    class CustomDate {

        @Autowired
        private Clock clock;

        @Test
        void testFixedClock() {
            assertEquals(CUSTOM_DATE, DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now(clock)));
        }
    }
}
