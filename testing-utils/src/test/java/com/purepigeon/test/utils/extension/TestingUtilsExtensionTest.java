package com.purepigeon.test.utils.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestingUtilsExtensionTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "arg0", "arg9", "arg99", "arg999", "arg9999"
    })
    void testNonPreservedParameterName(String parameterName) {
        // given
        TestingUtilsExtension extension = new TestingUtilsExtension();

        var parameterContext = mock(ParameterContext.class);
        var parameter = mock(Parameter.class);

        when(parameterContext.getParameter())
            .thenReturn(parameter);
        when(parameter.getName())
            .thenReturn(parameterName);

        var extensionContext = mock(ExtensionContext.class);

        // expect
        assertThrows(IllegalStateException.class, () -> extension.supportsParameter(parameterContext, extensionContext));
    }
}
