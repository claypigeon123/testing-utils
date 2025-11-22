package com.purepigeon.test.utils.mockwebserver.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import com.purepigeon.test.utils.mockwebserver.MockWebServerSupport;
import com.purepigeon.test.utils.mockwebserver.annotation.WithMockWebServer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.platform.testkit.engine.EngineTestKit;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;

public class PlainTestingUtilsMockWebServerExtensionTest {

    @Nested
    class MultipleFields {

        @Test
        void testMultipleBeans() {
            EngineTestKit.engine(JupiterEngineDescriptor.ENGINE_ID)
                .selectors(selectMethod(MultipleFields.InvalidUsageTest.class, "shouldFail"))
                .configurationParameter(JupiterConfiguration.DEACTIVATE_CONDITIONS_PATTERN_PROPERTY_NAME, "*")
                .execute()
                .testEvents()
                .assertThatEvents()
                .haveExactly(1, event(
                    finishedWithFailure(
                        instanceOf(IllegalStateException.class),
                        message("Expected a single MockWebServerSupport field")
                    )
                ));
        }

        @Nested
        @Disabled("Excluded from automatic global test run")
        @WithTestingUtils
        @WithMockWebServer
        @SuppressWarnings("unused")
        class InvalidUsageTest {

            private final TestingUtils testingUtils = new JacksonTestingUtils(new ObjectMapper());

            private final MockWebServerSupport mockWebServerSupport = MockWebServerSupport.createDefault(testingUtils);
            private final MockWebServerSupport secondMockWebServerSupport = MockWebServerSupport.createDefault(testingUtils);

            @Test
            void shouldFail() {
                fail("This should not be executed");
            }
        }
    }
}
