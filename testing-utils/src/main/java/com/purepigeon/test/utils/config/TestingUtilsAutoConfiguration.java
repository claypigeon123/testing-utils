package com.purepigeon.test.utils.config;

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

import com.google.gson.Gson;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.gson.GsonTestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import com.purepigeon.test.utils.impl.jsonb.JsonbTestingUtils;
import jakarta.json.bind.Jsonb;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.gson.autoconfigure.GsonAutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.jsonb.autoconfigure.JsonbAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.ObjectMapper;

/**
 * <p>
 *     Autoconfiguration for {@link TestingUtils}, when used with Spring Boot. See {@link WithTestingUtils}.
 * </p>
 * <p>
 *     Capable of registering a {@link TestingUtils} bean of type {@link JacksonTestingUtils},
 *     {@link GsonTestingUtils}, and {@link JsonbTestingUtils}, depending on what is available on the classpath.
 * </p>
 * <p>
 *     Important to note that the Jackson implementation bean is marked with the {@link Primary} annotation.
 * </p>
 */
@AutoConfiguration
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestingUtilsAutoConfiguration {

    private static final String JSONB_SPI = "classpath:META-INF/services/jakarta.json.bind.spi.JsonbProvider";
    private static final String JSON_SPI = "classpath:META-INF/services/jakarta.json.spi.JsonProvider";

    @Configuration
    @ConditionalOnClass(ObjectMapper.class)
    @Import(JacksonAutoConfiguration.class)
    public static class JacksonConfiguration {
        @Bean
        @Primary
        public TestingUtils jacksonTestingUtils(ObjectMapper objectMapper) {
            return new JacksonTestingUtils(objectMapper);
        }
    }

    @Configuration
    @ConditionalOnClass(Gson.class)
    @Import(GsonAutoConfiguration.class)
    public static class GsonConfiguration {
        @Bean
        public TestingUtils gsonTestingUtils(Gson gson) {
            return new GsonTestingUtils(gson);
        }
    }

    @Configuration
    @Import(JsonbAutoConfiguration.class)
    @ConditionalOnClass(Jsonb.class)
    @ConditionalOnResource(resources = { JSONB_SPI, JSON_SPI })
    public static class JsonbConfiguration {

        @Bean
        public TestingUtils jsonbTestingUtils(Jsonb jsonb) {
            return new JsonbTestingUtils(jsonb);
        }
    }
}
