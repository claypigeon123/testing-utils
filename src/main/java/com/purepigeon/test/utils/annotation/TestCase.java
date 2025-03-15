package com.purepigeon.test.utils.annotation;

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
 *     <pre>
 *     {@code
 *         @Test
 *         void testMethod(String testCase) {
 *             // ...
 *         }
 *     }
 *     </pre>
 *     The {@code testCase} value would be {@code testMethod}.
 * </p>
 * <p>
 *     And given the following test method:
 *     <pre>
 *     {@code
 *         @Test
 *         @TestCase("someTestMethod_200")
 *         void testMethod(String testCase) {
 *             // ...
 *         }
 *     }
 *     </pre>
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
     */
    String value();
}
