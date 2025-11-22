package com.purepigeon.test.utils.mockwebserver.annotation;

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
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.mockwebserver.MockWebServerSupport;
import com.purepigeon.test.utils.mockwebserver.config.TestingUtilsMockWebServerAutoConfiguration;
import com.purepigeon.test.utils.mockwebserver.extension.TestingUtilsMockWebServerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 *     Annotation to opt into mock web server functionalities facilitated by testing-utils. If using with spring,
 *     make sure there is a {@link TestingUtils} bean in the test application context by annotating your test class with
 *     {@link WithTestingUtils} as well.
 * </p>
 * <p>
 *     Registers the {@link TestingUtilsMockWebServerExtension} extension and adds a {@link MockWebServerSupport} bean
 *     to the application context via importing {@link TestingUtilsMockWebServerAutoConfiguration}.
 * </p>
 * <p>
 *     For example:
 * </p>
 * <pre>
 * {@code
 *     // ...
 *     @SpringBootTest
 *     @WithTestingUtils
 *     @WithMockWebServer
 *     class SampleServiceImplTest {
 *
 *         @Autowired
 *         private TestingUtils testingUtils;
 *
 *         @Autowired
 *         private MockWebServerSupport mockWebServerSupport;
 *
 *         // ...
 *     }
 * }
 * </pre>
 * <p>
 *     If Spring is not on the classpath, the initialization of a {@link MockWebServerSupport} has to be done manually,
 *     for example:
 * </p>
 * <pre>
 * {@code
 *     // ...
 *     @WithTestingUtils
 *     @WithMockWebServer
 *     class SampleTest {
 *
 *         private final TestingUtils testingUtils = new JacksonTestingUtils(new ObjectMapper());
 *
 *         private final MockWebServerSupport mockWebServerSupport = MockWebServerSupport.createDefault(testingUtils);
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
@ExtendWith(TestingUtilsMockWebServerExtension.class)
@Import(TestingUtilsMockWebServerAutoConfiguration.class)
public @interface WithMockWebServer {
    /**
     * <p>
     *     The port to start the mock server on.
     * </p>
     * <p>
     *     The default is 0, which means a random unused port will be selected - recommended to use it this way in most
     *     cases.
     * </p>
     * @return Port int value
     */
    int value() default 0;
}
