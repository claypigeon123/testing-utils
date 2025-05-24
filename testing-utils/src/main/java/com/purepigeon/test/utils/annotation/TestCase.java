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

import com.purepigeon.test.utils.extension.TestingUtilsExtension;

import java.lang.annotation.*;

/**
 * <p>
 *     Annotation to override the {@code testCase} argument for a given test method, resolved by
 *     {@link TestingUtilsExtension}.
 * </p>
 * <p>
 *     Omission of this annotation means that the {@code testCase} argument will be the test method name.
 * </p>
 * <p>
 *     For example, given the following test method:
 * </p>
 * <pre>
 * {@code
 *     @Test
 *     void testMethod(String testCase) {
 *         // ...
 *     }
 * }
 * </pre>
 * <p>
 *     The {@code testCase} value would be {@code testMethod}.
 * </p>
 * <p>
 *     And given the following test method:
 * </p>
 * <pre>
 * {@code
 *     @Test
 *     @TestCase("someTestMethod_200")
 *     void testMethod(String testCase) {
 *         // ...
 *     }
 * }
 * </pre>
 * <p>
 *     The {@code testCase} value would be {@code someTestMethod_200}.
 * </p>
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestCase {
    /**
     * The value to override the {@code testCase} test method argument with
     * @return The given test case override
     */
    String value();
}
