package com.purepigeon.test.utils.annotation;

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.config.TestingUtilsAutoConfiguration;
import com.purepigeon.test.utils.extension.TestingUtilsExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 *     Annotation to easily enable the functionalities provided by testing-utils. Requires an application context.
 * </p>
 * <p>
 *     Registers the {@link TestingUtilsExtension} extension, adds a {@link TestingUtils} bean to the application
 *     context via importing {@link TestingUtilsAutoConfiguration}.
 * </p>
 * <p>
 *     For example:
 * </p>
 * <pre>
 * {@code
 *     // ...
 *     @SpringBootTest
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
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(TestingUtilsExtension.class)
@Import(TestingUtilsAutoConfiguration.class)
public @interface WithTestingUtils {
}
