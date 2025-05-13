package com.purepigeon.test.utils.annotation;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.annotation.*;
import java.time.Clock;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@MockitoBean(types = Clock.class)
public @interface FixedClock {
    String DEFAULT_TIME = "2025-01-10T12:00:00.123Z";

    String value() default DEFAULT_TIME;
}
