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
 *         private TestingUtils testingUtils;
 *
 *         // ...
 *     }
 * }
 * </pre>
 * <p>
 *     If Spring is not on the classpath, the initialization of a {@link TestingUtils} has to be done manually, for
 *     example, to use Jackson for serialization and deserialization:
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
