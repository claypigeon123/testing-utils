package com.purepigeon.test.utils;

import com.purepigeon.test.utils.annotation.FixedClock;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@FixedClock
@WithTestingUtils
class PlainFixedClockTest {

    private static final String CUSTOM_INSTANT = "1990-01-01T12:00:00.234Z";

    private final Clock clock = mock();

    @Test
    void testFixedClock() {
        // when
        var result = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now(clock));

        // then
        assertEquals(FixedClock.DEFAULT_INSTANT, result);
    }

    @Test
    @FixedClock(CUSTOM_INSTANT)
    void testFixedClockModified() {
        // when
        var result = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now(clock));

        // then
        assertEquals(CUSTOM_INSTANT, result);
    }
}
