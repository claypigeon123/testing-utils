package com.purepigeon.test.utils.listener;

import com.purepigeon.test.utils.annotation.FixedClock;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.mockito.Mockito.when;

public class TestingUtilsTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) {
        FixedClock annotation = AnnotationUtils.findAnnotation(testContext.getTestClass(), FixedClock.class);
        if (annotation == null) return;

        mockClock(testContext, annotation.value());
    }

    // --

    private void mockClock(TestContext testContext, String value) {
        Instant instant = Instant.parse(value);
        Clock clock = testContext.getApplicationContext().getBean(Clock.class);
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }
}
