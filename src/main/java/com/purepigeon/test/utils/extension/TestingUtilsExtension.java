package com.purepigeon.test.utils.extension;

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.Suite;
import com.purepigeon.test.utils.annotation.TestCase;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import org.junit.jupiter.api.extension.*;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * <p>
 *     Junit 5 extension that sets the {@code suite} property in {@link TestingUtils}, and resolves the {@code testCase}
 *     arguments for test methods.
 * </p>
 * <p>
 *     See the documentation(s) of {@link Suite} and {@link TestCase} to customize behaviour.
 * </p>
 * <p>
 *     While this extension can be used directly with Junit's {@link ExtendWith} annotation, the simpler way is to just
 *     apply {@link WithTestingUtils} to a given test class instead.
 * </p>
 */
public class TestingUtilsExtension implements TestInstancePostProcessor, ParameterResolver {

    public static final String TEST_CASE_PARAM = "testCase";

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        Class<?> testClass = testInstance.getClass();
        String suite = Optional.ofNullable(testClass.getAnnotation(Suite.class))
            .map(Suite::value)
            .map(s -> s + "/" + testClass.getSimpleName())
            .orElse(testClass.getSimpleName());

        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        TestingUtils testingUtils = applicationContext.getBean(TestingUtils.class);
        testingUtils.setSuite(suite);
    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();

        return parameter.getName().equals(TEST_CASE_PARAM) && parameter.getType().equals(String.class);
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
