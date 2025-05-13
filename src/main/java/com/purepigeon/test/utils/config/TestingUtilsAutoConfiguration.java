package com.purepigeon.test.utils.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.FixedClock;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@Import(JacksonAutoConfiguration.class)
public class TestingUtilsAutoConfiguration {

    @Bean
    public TestingUtils jacksonTestingUtils(ObjectMapper objectMapper) {
        return new JacksonTestingUtils(objectMapper);
    }

    @Bean
    public Clock fixedClock() {
        Instant instant = Instant.parse(FixedClock.DEFAULT_TIME);
        return Clock.fixed(instant, ZoneOffset.UTC);
    }
}
