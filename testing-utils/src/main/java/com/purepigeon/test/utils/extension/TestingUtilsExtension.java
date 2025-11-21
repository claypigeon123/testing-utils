package com.purepigeon.test.utils.extension;

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

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.FixedClock;
import com.purepigeon.test.utils.annotation.Suite;
import com.purepigeon.test.utils.annotation.TestCase;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * <p>
 *     Junit 5 extension that sets the {@code suite} property in {@link TestingUtils}, resolves the {@code testCase}
 *     arguments for test methods, and handles {@link FixedClock} annotations.
 * </p>
 * <p>
 *     The extension works with or without Spring.
 * </p>
 * <p>
 *     See the documentation(s) of {@link Suite} and {@link TestCase} to customize behaviour.
 * </p>
 * <p>
 *     While this extension can be used directly with Junit's {@link ExtendWith} annotation, the simpler way is to just
 *     apply {@link WithTestingUtils} to a given test class.
 * </p>
 */
public class TestingUtilsExtension implements TestInstancePostProcessor, BeforeEachCallback, ParameterResolver  {

    /**
     * Test methods receive the test case name as this argument.
     */
    public static final String TEST_CASE_ARGUMENT_NAME = "testCase";

    private String suite;
    private boolean usesSpring = true;
    private boolean usesFixedClock = false;

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        Class<?> testClass = testInstance.getClass();

        String resolvedSuite = resolveSuite(testClass);

        this.usesFixedClock = testClass.isAnnotationPresent(FixedClock.class) || Arrays.stream(testClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Test.class))
            .anyMatch(method -> method.isAnnotationPresent(FixedClock.class));

        try {
            ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
            applicationContext.getBeansOfType(TestingUtils.class).values()
                .forEach(instance -> instance.setSuite(resolvedSuite));
        } catch (NoClassDefFoundError e) {
            this.usesSpring = false;
        } finally {
            this.suite = resolvedSuite;
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws IllegalAccessException {
        if (!usesSpring) setSuiteForNonSpringUsage(context);
        if (usesFixedClock && usesSpring) mockSpringClock(context);
        if (usesFixedClock && !usesSpring) mockPlainClock(context);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();

        return parameter.getName().equals(TEST_CASE_ARGUMENT_NAME) && parameter.getType().equals(String.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Optional<Method> testMethodOpt = extensionContext.getTestMethod();
        if (testMethodOpt.isEmpty()) return null;

        Method method = testMethodOpt.get();

        return Optional.ofNullable(method.getAnnotation(TestCase.class))
            .map(TestCase::value)
            .orElse(method.getName());
    }

    // --

    private String resolveSuite(Class<?> testClass) {
        Suite suiteAnnotation = testClass.getAnnotation(Suite.class);

        if (suiteAnnotation == null) {
            return testClass.getSimpleName();
        }
        if (suiteAnnotation.value().isBlank()) {
            throw new IllegalStateException("@Suite value cannot be blank - please provide a non-blank value or remove the annotation completely to use the default suite name (" + testClass.getSimpleName() + ")");
        }

        return suiteAnnotation.value() + (suiteAnnotation.appendClassName() ? "/" + testClass.getSimpleName() : "");
    }

    private void setSuiteForNonSpringUsage(ExtensionContext context) throws IllegalAccessException {
        Object testInstance = context.getTestInstance().orElse(null);
        if (testInstance == null) return;

        Field testingUtilsField = getTestClassField(testInstance, TestingUtils.class).orElse(null);
        if (testingUtilsField == null) return;

        testingUtilsField.setAccessible(true);
        TestingUtils testingUtils = (TestingUtils) testingUtilsField.get(testInstance);
        testingUtils.setSuite(suite);
        testingUtilsField.setAccessible(false);
    }

    private void mockSpringClock(ExtensionContext context) {
        Instant instant = getFixedClockValue(context);

        Clock clock = SpringExtension.getApplicationContext(context).getBean(Clock.class);
        mockClock(clock, instant);
    }

    private void mockPlainClock(ExtensionContext context) throws IllegalAccessException {
        Instant instant = getFixedClockValue(context);

        Object testInstance = context.getTestInstance().orElse(null);
        if (testInstance == null) return;

        Field clockField = getTestClassField(testInstance, Clock.class).orElse(null);
        if (clockField == null) return;

        clockField.setAccessible(true);
        Clock clock = (Clock) clockField.get(testInstance);
        mockClock(clock, instant);
        clockField.setAccessible(false);
    }

    private Optional<Field> getTestClassField(Object testInstance, Class<?> clazz) {
        return Arrays.stream(testInstance.getClass().getDeclaredFields())
            .filter(field -> field.getType().isAssignableFrom(clazz))
            .findFirst();
    }

    private void mockClock(Clock clock, Instant instant) {
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }

    private Instant getFixedClockValue(ExtensionContext context) {
        FixedClock classAnnotation = context.getTestClass()
            .map(clazz -> clazz.getDeclaredAnnotation(FixedClock.class))
            .orElse(null);

        if (classAnnotation == null) {
            throw new IllegalStateException("Also annotate your test class with @FixedClock - it cannot be used solely on the method level");
        }

        FixedClock methodAnnotation = context.getTestMethod()
            .map(method -> method.getDeclaredAnnotation(FixedClock.class))
            .orElse(null);

        String value = Optional.of(Optional.ofNullable(methodAnnotation).orElse(classAnnotation))
            .map(FixedClock::value)
            .orElse(FixedClock.DEFAULT_INSTANT);

        return Instant.parse(value);
    }
}
