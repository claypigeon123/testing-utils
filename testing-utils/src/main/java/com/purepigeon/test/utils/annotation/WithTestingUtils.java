package com.purepigeon.test.utils.annotation;

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.config.TestingUtilsAutoConfiguration;
import com.purepigeon.test.utils.extension.TestingUtilsExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 *     Annotation to easily enable the functionalities provided by Testing Utils.
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
 * <p>
 *     If Spring is not on the classpath, the initialization of a {@link TestingUtils} has to be done manually:
 * </p>
 * <pre>
 * {@code
 *     // ...
 *     @WithTestingUtils
 *     class SampleTest {
 *
 *         private final TestingUtils testingUtils = new JacksonTestingUtils(new ObjectMapper());
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
