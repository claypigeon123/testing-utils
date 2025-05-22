package com.purepigeon.test.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 *     Autoconfiguration for {@link TestingUtils}, when used with Spring Boot. See {@link WithTestingUtils}.
 * </p>
 * <p>
 *     If Jackson is on the classpath, a {@link TestingUtils} bean will be registered that uses the Jackson
 *     {@link ObjectMapper} for serialization and deserialization.
 * </p>
 */
@AutoConfiguration
public class TestingUtilsAutoConfiguration {

    @Configuration
    @ConditionalOnClass(ObjectMapper.class)
    @Import(JacksonAutoConfiguration.class)
    public static class JacksonConfiguration {
        @Bean
        public TestingUtils jacksonTestingUtils(ObjectMapper objectMapper) {
            return new JacksonTestingUtils(objectMapper);
        }
    }
}
