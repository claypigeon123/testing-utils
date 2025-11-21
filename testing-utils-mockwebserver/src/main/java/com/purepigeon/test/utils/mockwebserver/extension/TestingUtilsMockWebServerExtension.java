package com.purepigeon.test.utils.mockwebserver.extension;

/*-
 * #%L
 * Testing Utils
 * %%
 * Copyright (C) 2025 Purepigeon
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

import com.purepigeon.test.utils.mockwebserver.MockWebServerSupport;
import com.purepigeon.test.utils.mockwebserver.annotation.MockWebServerlessTest;
import com.purepigeon.test.utils.mockwebserver.annotation.WithMockWebServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestingUtilsMockWebServerExtension implements TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback {

    private int port = 0;
    private boolean usesSpring = true;

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        Class<?> testClass = testInstance.getClass();
        this.port = resolvePort(testClass);

        try {
            SpringExtension.getApplicationContext(context);
        } catch (NoClassDefFoundError e) {
            this.usesSpring = false;
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        if (isOptedOutTestMethod(context.getRequiredTestMethod())) return;

        getMockWebServerSupportInstance(context)
            .ifPresent(mockWebServerSupport -> mockWebServerSupport.start(this.port));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (isOptedOutTestMethod(context.getRequiredTestMethod())) return;

        getMockWebServerSupportInstance(context)
            .ifPresent(MockWebServerSupport::close);
    }

    // --

    private int resolvePort(Class<?> testClass) {
        WithMockWebServer annotation = testClass.getAnnotation(WithMockWebServer.class);

        if (annotation == null) return 0;

        return annotation.value();
    }

    private Optional<MockWebServerSupport> getMockWebServerSupportInstance(ExtensionContext context) {
        return usesSpring
            ? getSpringMockWebServerSupportInstance(context)
            : getNonSpringMockWebServerSupportInstance(context);
    }

    private Optional<MockWebServerSupport> getSpringMockWebServerSupportInstance(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);

        List<MockWebServerSupport> beans = applicationContext.getBeansOfType(MockWebServerSupport.class).values()
            .stream()
            .toList();

        if (beans.size() != 1) throw new IllegalStateException("Expected a single MockWebServerSupport bean");

        return Optional.of(beans.getFirst());
    }

    @SneakyThrows
    private Optional<MockWebServerSupport> getNonSpringMockWebServerSupportInstance(ExtensionContext context) {
        Object testInstance = context.getTestInstance().orElse(null);
        if (testInstance == null) return Optional.empty();

        Field field = getMockWebServerSupportField(testInstance);
        field.setAccessible(true);
        MockWebServerSupport instance = (MockWebServerSupport) field.get(testInstance);
        field.setAccessible(false);

        return Optional.of(instance);
    }

    private Field getMockWebServerSupportField(Object testInstance) {
        List<Field> fields = Arrays.stream(testInstance.getClass().getDeclaredFields())
            .filter(field -> field.getType().isAssignableFrom(MockWebServerSupport.class))
            .toList();

        if (fields.size() != 1) throw new IllegalStateException("Expected a single MockWebServerSupport field");

        return fields.getFirst();
    }

    private boolean isOptedOutTestMethod(Method testMethod) {
        return testMethod.isAnnotationPresent(MockWebServerlessTest.class);
    }
}
