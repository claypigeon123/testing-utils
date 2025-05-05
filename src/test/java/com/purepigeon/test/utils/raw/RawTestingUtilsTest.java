package com.purepigeon.test.utils.raw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.Suite;
import com.purepigeon.test.utils.annotation.TestCase;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import com.purepigeon.test.utils.test.TestData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Suite("raw")
@WithTestingUtils(useSpring = false)
class RawTestingUtilsTest {

    private final TestingUtils testingUtils = new JacksonTestingUtils(new ObjectMapper());

    @Test
    void testRaw(String testCase) {
        var data = testingUtils.readInputObject(testCase, TestData.class);

        assertEquals("raw/RawTestingUtilsTest", testingUtils.getSuite());
        assertEquals("testRaw", testCase);
        testingUtils.assertObject(testCase, data);
    }

    @Test
    @TestCase("testRaw_2")
    void testRaw_overriddenTestCase(String testCase) {
        var data = testingUtils.readInputObject(testCase, TestData.class);

        assertEquals("raw/RawTestingUtilsTest", testingUtils.getSuite());
        assertEquals("testRaw_2", testCase);
        testingUtils.assertObject(testCase, data);
    }
}
