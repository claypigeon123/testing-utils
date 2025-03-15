package com.purepigeon.test.utils.annotation;

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.extension.TestingUtilsExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(TestingUtilsExtension.class)
@Import({
    JacksonAutoConfiguration.class,
    TestingUtils.class
})
public @interface WithTestingUtils {
}
