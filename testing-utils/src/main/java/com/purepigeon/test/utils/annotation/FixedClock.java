package com.purepigeon.test.utils.annotation;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.annotation.*;
import java.time.Clock;

/**
 * <p>
 *     Annotation to override / register a bean of type {@link Clock} in the application context using a fixed
 *     date-time.
 * </p>
 * <p>
 *     Can be used on the type level or method level; however, method level usage is only supported if there is a type
 *     level annotation present as well.
 * </p>
 * <p>
 *     Requires usage of {@link WithTestingUtils}.
 * </p>
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@MockitoBean(types = Clock.class)
public @interface FixedClock {
    /**
     * <p>
     *     The default ISO date-time value for the fixed {@link Clock} bean.
     * </p>
     */
    String DEFAULT_INSTANT = "2025-01-10T12:30:15.123Z";

    /**
     * <p>
     *     The ISO date-time value to fix the {@link Clock} bean to.
     * </p>
     * <p>
     *     Default is {@link FixedClock#DEFAULT_INSTANT}.
     * </p>
     */
    String value() default DEFAULT_INSTANT;
}
