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
