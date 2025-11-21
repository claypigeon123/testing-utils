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

import com.purepigeon.test.utils.mockwebserver.config.TestingUtilsMockWebServerAutoConfiguration;
import com.purepigeon.test.utils.mockwebserver.extension.TestingUtilsMockWebServerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

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
     *     The default is 0, which means a random unused port will be selected - recommended to use this way.
     * </p>
     * @return The fixed instant in string format.
     */
    int value() default 0;
}
