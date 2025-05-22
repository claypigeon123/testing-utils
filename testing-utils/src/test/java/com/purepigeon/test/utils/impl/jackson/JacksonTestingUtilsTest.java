package com.purepigeon.test.utils.impl.jackson;

import com.purepigeon.test.utils.AbstractTestingUtilsTest;
import com.purepigeon.test.utils.TestingUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class JacksonTestingUtilsTest extends AbstractTestingUtilsTest {

    @Override
    @Autowired
    protected void setTestingUtils(@Qualifier("jacksonTestingUtils") TestingUtils testingUtils) {
        this.testingUtils = testingUtils;
    }

    @Test
    @Override
    public void assertImpl() {
        assertInstanceOf(JacksonTestingUtils.class, testingUtils);
    }
}
