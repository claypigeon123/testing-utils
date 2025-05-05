package com.purepigeon.test.utils.annotation;

import com.purepigeon.test.utils.config.TestingUtilsAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithTestingUtils
@SpringBootTest(classes = TestingUtilsAutoConfiguration.class)
class TestCaseTest {

    @Test
    @TestCase("getTestCase_123")
    void getTestCase(String testCase) {
        assertEquals("getTestCase_123", testCase);
    }
}
