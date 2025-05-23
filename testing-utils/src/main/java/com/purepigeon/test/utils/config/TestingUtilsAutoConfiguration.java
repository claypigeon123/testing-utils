package com.purepigeon.test.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.gson.GsonTestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import com.purepigeon.test.utils.impl.jsonb.JsonbTestingUtils;
import jakarta.json.bind.Jsonb;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

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
public class TestingUtilsAutoConfiguration {

    private static final String JSONB_BIND_SPI = "classpath:META-INF/services/jakarta.json.bind.spi.JsonbProvider";
    private static final String JSONB_SPI = "classpath:META-INF/services/jakarta.json.spi.JsonProvider";

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
    @ConditionalOnResource(resources = { JSONB_BIND_SPI, JSONB_SPI })
    public static class JsonbConfiguration {
        @Bean
        public TestingUtils jsonbTestingUtils(Jsonb jsonb) {
            return new JsonbTestingUtils(jsonb);
        }
    }
}
