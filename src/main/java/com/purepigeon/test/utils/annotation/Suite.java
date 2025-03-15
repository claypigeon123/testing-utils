package com.purepigeon.test.utils.annotation;

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
 *     <pre>
 *     {@code
 *         @WithTestingUtils
 *         @Suite("service/impl")
 *         void SampleServiceImplTest {
 *
 *             @Autowired
 *             private TestingUtils testingUtils;
 *
 *             // ...
 *         }
 *     }
 *     </pre>
 *     {@code testingUtils.getSuite()} would return {@code service/impl/SampleServiceImplTest}.
 * </p>
 * <p>
 *     And given the following:
 *     <pre>
 *     {@code
 *         @WithTestingUtils
 *         void SampleServiceImplTest {
 *
 *             @Autowired
 *             private TestingUtils testingUtils;
 *
 *             // ...
 *         }
 *     }
 *     </pre>
 *     {@code testingUtils.getSuite()} would return {@code SampleServiceImplTest}.
 * </p>
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Suite {
    /**
     * Prefix to apply to {@link TestingUtils#getSuite()}
     */
    String value();
}
