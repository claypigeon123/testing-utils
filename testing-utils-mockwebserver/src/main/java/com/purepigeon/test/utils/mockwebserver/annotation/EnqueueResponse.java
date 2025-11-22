package com.purepigeon.test.utils.mockwebserver.annotation;

import com.purepigeon.test.utils.DefaultArtifactType;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(EnqueueResponse.Wrapper.class)
public @interface EnqueueResponse {
    String artifactType() default DefaultArtifactType.INPUT;

    Class<?> value() default Void.class;

    String artifactName() default "";

    int status() default 200;

    @Inherited
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Wrapper {
        EnqueueResponse[] value();
    }
}
