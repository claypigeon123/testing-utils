package com.purepigeon.test.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
