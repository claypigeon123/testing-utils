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
import com.purepigeon.test.utils.extension.TestingUtilsExtension;

import java.lang.annotation.*;

/**
 * <p>
 *     Annotation to prefix the suite returned by {@link TestingUtils#getSuite()}, resolved automatically via
 *     {@link TestingUtilsExtension}. Useful for fine-grained organization of test resources.
 * </p>
 * <p>
 *     Omission of this annotation would mean that {@link TestingUtils#getSuite()} returns the simple classname of the
 *     given test class.
 * </p>
 * <p>
 *     For example, given the following:
 * </p>
 * <pre>
 * {@code
 *     // ...
 *     @WithTestingUtils
 *     @Suite("service/impl")
 *     class SampleServiceImplTest {
 *
 *         @Autowired
 *         private TestingUtils testingUtils
 *
 *         // ...
 *     }
 * }
 * </pre>
 * <p>
 *     {@code testingUtils.getSuite()} would return {@code service/impl/SampleServiceImplTest}.
 * </p>
 * <p>
 *     And given the following:
 * </p>
 * <pre>
 * {@code
 *     // ...
 *     @WithTestingUtils
 *     class SampleServiceImplTest {
 *
 *         @Autowired
 *         private TestingUtils testingUtils
 *
 *         // ...
 *     }
 * }
 * </pre>
 * <p>
 *     {@code testingUtils.getSuite()} would return {@code SampleServiceImplTest}.
 * </p>
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Suite {
    /**
     * <p>
     *     The prefix to apply to the suite value returned by {@link TestingUtils#getSuite()}.
     * </p>
     * <p>
     *     If {@link Suite#appendClassName()} is set to {@code false}, the value provided here is no longer just a
     *     prefix, but rather the entire suite value.
     * </p>
     * @return The given prefix / suite value. Defaults to an empty string.
     */
    String value() default "";

    /**
     * Whether to append a forward slash and the simple classname of the test class to the suite value.
     * @return The given append flag. Defaults to {@code true}.
     */
    boolean appendClassName() default true;
}
