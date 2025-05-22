package com.purepigeon.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.annotation.Suite;
import com.purepigeon.test.utils.annotation.TestCase;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Suite("plain")
@WithTestingUtils
class PlainUsageTest {

    private static final String EXPECTED_SUITE = "plain/%s".formatted(PlainUsageTest.class.getSimpleName());

    private final TestingUtils testingUtils = new JacksonTestingUtils(new ObjectMapper());

    @Test
    void defaultTestCaseTest(String testCase) {
        assertEquals(EXPECTED_SUITE, testingUtils.getSuite());
        assertEquals("defaultTestCaseTest", testCase);
    }

    @Test
    @TestCase("overrideTestCaseTest_123")
    void overrideTestCaseTest(String testCase) {
        assertEquals(EXPECTED_SUITE, testingUtils.getSuite());
        assertEquals("overrideTestCaseTest_123", testCase);
    }
}
