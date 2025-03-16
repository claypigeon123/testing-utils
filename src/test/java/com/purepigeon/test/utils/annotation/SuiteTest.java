package com.purepigeon.test.utils.annotation;

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.setup.TestApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithTestingUtils
@Suite("annotation")
@SpringBootTest(classes = TestApp.class)
class SuiteTest {

    private static final String EXPECTED_SUITE = "annotation/" + SuiteTest.class.getSimpleName();

    @Autowired
    private TestingUtils testingUtils;

    @Test
    void getSuite() {
        assertEquals(EXPECTED_SUITE, testingUtils.getSuite());
    }
}
