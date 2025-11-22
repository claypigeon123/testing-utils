package com.purepigeon.test.utils.mockwebserver.extension;

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.mockwebserver.MockWebServerSupport;
import com.purepigeon.test.utils.mockwebserver.annotation.EnqueueResponse;
import com.purepigeon.test.utils.mockwebserver.annotation.WithMockWebServer;
import com.purepigeon.test.utils.mockwebserver.test.TestApp;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

class TestingUtilsMockWebServerExtensionTest {

    @Nested
    class MultipleBeans {

        @Test
        void testMultipleBeans() {
            testFailedToStart(
                MultipleBeans.InvalidUsageTest.class,
                "Expected a single MockWebServerSupport bean"
            );
        }

        @Nested
        @Disabled("Excluded from automatic global test run")
        @WithTestingUtils
        @WithMockWebServer
        @SpringBootTest(classes = {TestApp.class, MultipleBeans.InvalidUsageTest.Config.class})
        class InvalidUsageTest {
            @Test
            void shouldFail() {
                fail("This should not be executed");
            }

            @TestConfiguration
            static class Config {
                @Bean
                public MockWebServerSupport secondMockWebServerSupport(TestingUtils testingUtils) {
                    return MockWebServerSupport.createDefault(testingUtils);
                }
            }
        }
    }

    @Nested
    class MethodLevelEnqueue_WithNoClassLevel {

        @Test
        void testEnqueueResponse() {
            testFailedToStart(
                MethodLevelEnqueue_WithNoClassLevel.InvalidUsageTest.class,
                "@EnqueueResponse annotation must specify either the 'value' or the 'artifactName' parameter (when both are defined, 'artifactName' is ignored)"
            );
        }

        @Nested
        @Disabled("Excluded from automatic global test run")
        @WithTestingUtils
        @WithMockWebServer
        @SpringBootTest(classes = TestApp.class)
        class InvalidUsageTest {
            @Test
            @EnqueueResponse
            void shouldFail() {
                fail("This should not be executed");
            }
        }
    }

    private void testFailedToStart(Class<?> testClass, String expectedMessage) {
        EngineTestKit.engine(JupiterEngineDescriptor.ENGINE_ID)
            .selectors(selectMethod(testClass, "shouldFail"))
            .configurationParameter(JupiterConfiguration.DEACTIVATE_CONDITIONS_PATTERN_PROPERTY_NAME, "*")
            .execute()
            .testEvents()
            .assertThatEvents()
            .haveExactly(1, event(
                finishedWithFailure(
                    instanceOf(IllegalStateException.class),
                    message(expectedMessage)
                )
            ));
    }
}
