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
     * @return The fixed instant in string format.
     */
    String value() default DEFAULT_INSTANT;
}
