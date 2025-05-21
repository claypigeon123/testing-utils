package com.purepigeon.test.utils.extension;

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
 *     apply {@link WithTestingUtils} to a given test class if Spring is being used.
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
        String suite = Optional.ofNullable(testClass.getAnnotation(Suite.class))
            .map(Suite::value)
            .map(s -> s + "/" + testClass.getSimpleName())
            .orElse(testClass.getSimpleName());

        WithTestingUtils annotation = testClass.getAnnotation(WithTestingUtils.class);

        if (annotation == null) {
            throw new IllegalStateException("No @WithTestingUtils annotation found");
        }

        this.usesFixedClock = testClass.isAnnotationPresent(FixedClock.class) || Arrays.stream(testClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Test.class))
            .anyMatch(method -> method.isAnnotationPresent(FixedClock.class));

        try {
            ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
            TestingUtils testingUtils = applicationContext.getBean(TestingUtils.class);
            testingUtils.setSuite(suite);
        } catch (NoClassDefFoundError e) {
            this.usesSpring = false;
        } finally {
            this.suite = suite;
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws IllegalAccessException {
        if (!usesSpring) setSuiteForNonSpringUsage(context);
        if (usesSpring && usesFixedClock) mockClock(context);
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

    private void setSuiteForNonSpringUsage(ExtensionContext context) throws IllegalAccessException {
        Object testInstance = context.getTestInstance()
            .orElse(null);

        if (testInstance == null) return;

        Field testingUtilsField = Arrays.stream(testInstance.getClass().getDeclaredFields())
            .filter(field -> field.getType().isAssignableFrom(TestingUtils.class))
            .findFirst()
            .orElse(null);

        if (testingUtilsField == null) return;

        testingUtilsField.setAccessible(true);
        TestingUtils testingUtils = (TestingUtils) testingUtilsField.get(testInstance);
        testingUtils.setSuite(suite);
        testingUtilsField.setAccessible(false);
    }

    private void mockClock(ExtensionContext context) {
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
            .orElse(FixedClock.DEFAULT_TIME);

        Instant instant = Instant.parse(value);
        Clock clock = SpringExtension.getApplicationContext(context).getBean(Clock.class);
        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }
}
