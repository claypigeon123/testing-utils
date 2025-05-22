package com.purepigeon.test.utils.impl.gson;

import com.purepigeon.test.utils.AbstractTestingUtilsTest;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.Suite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@Suite(value = "TestingUtilsTest", appendClassName = false)
public class GsonTestingUtilsTest extends AbstractTestingUtilsTest {

    @Override
    @Autowired
    protected void setTestingUtils(@Qualifier("gsonTestingUtils") TestingUtils testingUtils) {
        this.testingUtils = testingUtils;
    }

    @Test
    void assertImpl() {
        assertInstanceOf(GsonTestingUtils.class, testingUtils);
    }

    @Test
    void getSuite() {
        assertEquals("TestingUtilsTest", testingUtils.getSuite());
    }
}
