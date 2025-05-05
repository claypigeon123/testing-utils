package com.purepigeon.test.utils.extension;

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.Suite;
import com.purepigeon.test.utils.annotation.TestCase;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import org.junit.jupiter.api.extension.*;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

/**
 * <p>
 *     Junit 5 extension that sets the {@code suite} property in {@link TestingUtils}, and resolves the {@code testCase}
 *     arguments for test methods.
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

        if (Boolean.FALSE.equals(annotation.useSpring())) {
            this.suite = suite;
            return;
        }

        try {
            ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
            TestingUtils testingUtils = applicationContext.getBean(TestingUtils.class);
            testingUtils.setSuite(suite);
        } catch (NoClassDefFoundError e) {
            throw new IllegalStateException("Are you using Testing Utils without spring? Make sure to provide \"useSpring = false\" in @WithTestingUtils", e);
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws IllegalAccessException {
        if (suite == null) return;

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
}
