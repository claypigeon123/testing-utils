package com.purepigeon.test.utils.extension;

/*-
 * #%L
 * Testing Utils
 * %%
 * Copyright (C) 2025 - 2026 Purepigeon
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
