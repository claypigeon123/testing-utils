package com.purepigeon.test.utils.mockwebserver.annotation;

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

import com.purepigeon.test.utils.DefaultArtifactType;
import com.purepigeon.test.utils.TestingUtils;

import java.lang.annotation.*;

/**
 * <p>
 *     Annotation to declaratively enqueue a response for the mock web server. This annotation is repeatable, meaning
 *     that multiple responses can be enqueued declaratively.
 * </p>
 * <p>
 *     Take care that either {@link EnqueueResponse#value} or {@link EnqueueResponse#artifactName} <b>must</b> be
 *     specified, or a runtime exception will be thrown.
 * </p>
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(EnqueueResponse.Wrapper.class)
public @interface EnqueueResponse {
    /**
     * <p> The artifact type to enqueue. </p>
     * <p> The default is {@link DefaultArtifactType#INPUT} </p>
     * @return Artifact type
     */
    String artifactType() default DefaultArtifactType.INPUT;

    /**
     * <p>
     *     Class of the artifact to enqueue. This is used by {@link TestingUtils#artifactFileName(Class)} to resolve a
     *     file name for the class.
     * </p>
     * <p>
     *     If {@link EnqueueResponse#artifactName} is not provided, then this <b>must</b> be provided. If both are
     *     provided, this parameter takes priority.
     * </p>
     * @return Artifact class
     */
    Class<?> value() default Void.class;

    /**
     * <p> Name of the artifact to enqueue. </p>
     * <p>
     *     If {@link EnqueueResponse#value} is not provided, then this <b>must</b> be provided. If both are provided,
     *     {@link EnqueueResponse#value} takes priority.
     * </p>
     * @return Artifact name
     */
    String artifactName() default "";

    /**
     * <p> Response status. </p>
     * <p> The default is {@code 200}. </p>
     * @return Response status
     */
    int status() default 200;

    /**
     * <p>
     *     Value of the Content-Type header in the response. Provide a blank string to not include a Content-Type header.
     * </p>
     * <p> The default is {@code application/json}. </p>
     * @return Content type value
     */
    String contentType() default "application/json";

    /**
     * <p> Helper annotation for repeatability. </p>
     */
    @Inherited
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Wrapper {
        EnqueueResponse[] value();
    }
}
