package com.purepigeon.test.utils;

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

import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.AbstractTestingUtils;
import com.purepigeon.test.utils.impl.gson.GsonTestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import com.purepigeon.test.utils.impl.jsonb.JsonbTestingUtils;
import lombok.SneakyThrows;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * <p>
 *     Provides various testing utilities, including:
 * </p>
 * <ul>
 *     <li>Loading input / expected artifacts as objects or strings from test resources</li>
 *     <li>Asserting on test results using expected artifacts from test resources</li>
 *     <li>Converting JSON strings to objects / vice versa</li>
 * </ul>
 * <p>
 *     Custom implementations of this interface should extend {@link AbstractTestingUtils} to inherit handling for
 *     'suite', which is likely common, regardless of implementation.
 * </p>
 * <hr />
 * <p>
 *     See the {@link WithTestingUtils} annotation for more information on how to facilitate usage.
 * </p>
 * <hr />
 * <p>
 *     This library provides three implementations, including Spring Boot autoconfiguration:
 * </p>
 * <ul>
 *     <li>{@link JacksonTestingUtils}</li>
 *     <li>{@link GsonTestingUtils}</li>
 *     <li>{@link JsonbTestingUtils}</li>
 * </ul>
 */
public interface TestingUtils {
    /**
     * <p>
     *     Reads an input JSON test resource as the specified type.
     * </p>
     * <p>
     *     Expects the resource to have the same name as the given type's simple name. For example, specifying
     *     {@code TestRequest.class} as the type would read the resource from
     *     {@code {suite}/{testCase}/input/TestRequest.json}.
     * </p>
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    default <T> T readInputObject(String testCase, Class<T> returnObjectType) {
        return readInputObject(testCase, artifactFileName(returnObjectType), returnObjectType);
    }

    /**
     * <p>
     *     Reads an input JSON test resource as the specified type.
     * </p>
     * <p>
     *     Expects the resource to have the same name as the given type's simple name. For example, specifying
     *     {@code new TypeRef<TestRequest>() {}} as the type would read the resource from
     *     {@code {suite}/{testCase}/input/TestRequest.json}.
     * </p>
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    default <T> T readInputObject(String testCase, TypeRef<T> returnObjectType) {
        return readInputObject(testCase, artifactFileName(returnObjectType), returnObjectType);
    }

    /**
     * <p>
     *     Reads an input test resource as the specified type, with the specified filename.
     * </p>
     * <p>
     *     Unlike the variants where the {@code artifactName} argument is omitted, this variant does not infer the
     *     test resource name from the type, but uses the {@code artifactName} directly.
     * </p>
     * <p>
     *     For example, calling this method with an artifactName of {@code TestRequestModified.json} and a
     *     returnObjectType of {@code TestRequest.class}, the test resource would be read from
     *     {@code {suite}/{testCase}/input/TestRequestModified.json}, returned as an instance of {@code TestRequest}.
     * </p>
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    default <T> T readInputObject(String testCase, String artifactName, Class<T> returnObjectType) {
        return readObject(testCase, DefaultArtifactType.INPUT, artifactName, returnObjectType);
    }

    /**
     * <p>
     *     Reads an input test resource as the specified type, with the specified filename.
     * </p>
     * <p>
     *     Unlike the variants where the {@code artifactName} argument is omitted, this variant does not infer the
     *     test resource name from the type, but uses the {@code artifactName} directly.
     * </p>
     * <p>
     *     For example, calling this method with an artifactName of {@code TestRequestModified.json} and a
     *     returnObjectType of {@code new TypeRef<TestRequest>() {}}, the test resource would be read from
     *     {@code {suite}/{testCase}/input/TestRequestModified.json}, returned as an instance of {@code TestRequest}.
     * </p>
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    default <T> T readInputObject(String testCase, String artifactName, TypeRef<T> returnObjectType) {
        return readObject(testCase, DefaultArtifactType.INPUT, artifactName, returnObjectType);
    }

    /**
     * <p>
     *     Reads an expected JSON test resource as the specified type.
     * </p>
     * <p>
     *     Expects the resource to have the same name as the given type's simple name. For example, specifying
     *     {@code TestRequest.class} as the type would read the resource from
     *     {@code {suite}/{testCase}/expected/TestRequest.json}.
     * </p>
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    default <T> T readExpectedObject(String testCase, Class<T> returnObjectType) {
        return readExpectedObject(testCase, artifactFileName(returnObjectType), returnObjectType);
    }

    /**
     * <p>
     *     Reads an expected JSON test resource as the specified type.
     * </p>
     * <p>
     *     Expects the resource to have the same name as the given type's simple name. For example, specifying
     *     {@code new TypeRef<TestRequest>() {}} as the type would read the resource from
     *     {@code {suite}/{testCase}/expected/TestRequest.json}.
     * </p>
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    default <T> T readExpectedObject(String testCase, TypeRef<T> returnObjectType) {
        return readExpectedObject(testCase, artifactFileName(returnObjectType), returnObjectType);
    }

    /**
     * <p>
     *     Reads an expected test resource as the specified type, with the specified filename.
     * </p>
     * <p>
     *     Unlike the variants where the {@code artifactName} argument is omitted, this variant does not infer the
     *     test resource name from the type, but uses the {@code artifactName} directly.
     * </p>
     * <p>
     *     For example, calling this method with an artifactName of {@code TestRequestModified.json} and a
     *     returnObjectType of {@code TestRequest.class}, the test resource would be read from
     *     {@code {suite}/{testCase}/expected/TestRequestModified.json}, returned as an instance of {@code TestRequest}.
     * </p>
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    default <T> T readExpectedObject(String testCase, String artifactName, Class<T> returnObjectType) {
        return readObject(testCase, DefaultArtifactType.EXPECTED, artifactName, returnObjectType);
    }

    /**
     * <p>
     *     Reads an expected test resource as the specified type, with the specified filename.
     * </p>
     * <p>
     *     Unlike the variants where the {@code artifactName} argument is omitted, this variant does not infer the
     *     test resource name from the type, but uses the {@code artifactName} directly.
     * </p>
     * <p>
     *     For example, calling this method with an artifactName of {@code TestRequestModified.json} and a
     *     returnObjectType of {@code new TypeRef<TestRequest>() {}}, the test resource would be read from
     *     {@code {suite}/{testCase}/expected/TestRequestModified.json}, returned as an instance of {@code TestRequest}.
     * </p>
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    default <T> T readExpectedObject(String testCase, String artifactName, TypeRef<T> returnObjectType) {
        return readObject(testCase, DefaultArtifactType.EXPECTED, artifactName, returnObjectType);
    }

    /**
     * <p>
     *     The full method for reading input and expected test resources as objects.
     * </p>
     * <p>
     *     Prefer using the more streamlined variants:
     * </p>
     * <ul>
     *     <li>{@link TestingUtils#readInputObject(String, Class)}</li>
     *     <li>{@link TestingUtils#readInputObject(String, String, Class)}</li>
     *     <li>{@link TestingUtils#readExpectedObject(String, Class)}</li>
     *     <li>{@link TestingUtils#readExpectedObject(String, String, Class)}</li>
     * </ul>
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     * @deprecated Prefer {@link TestingUtils#readObject(String, String, String, Class)}.
     *             Subject to removal in the next major version 2.0.0.
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    default <T> T readObject(String testCase, ArtifactType artifactType, String artifactName, Class<T> returnObjectType) {
        return readObject(testCase, artifactType.toString(), artifactName, returnObjectType);
    }

    /**
     * <p>
     *     The full method for reading input and expected test resources as objects.
     * </p>
     * <p>
     *     Prefer using the more streamlined variants:
     * </p>
     * <ul>
     *     <li>{@link TestingUtils#readInputObject(String, Class)}</li>
     *     <li>{@link TestingUtils#readInputObject(String, String, Class)}</li>
     *     <li>{@link TestingUtils#readExpectedObject(String, Class)}</li>
     *     <li>{@link TestingUtils#readExpectedObject(String, String, Class)}</li>
     * </ul>
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type (defaults in {@link DefaultArtifactType})
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    <T> T readObject(String testCase, String artifactType, String artifactName, Class<T> returnObjectType);

    /**
     * <p>
     *     The full method for reading input and expected test resources as objects.
     * </p>
     * <p>
     *     Prefer using the more streamlined variants:
     * </p>
     * <ul>
     *     <li>{@link TestingUtils#readInputObject(String, TypeRef)}</li>
     *     <li>{@link TestingUtils#readInputObject(String, String, TypeRef)}</li>
     *     <li>{@link TestingUtils#readExpectedObject(String, TypeRef)}</li>
     *     <li>{@link TestingUtils#readExpectedObject(String, String, TypeRef)}</li>
     * </ul>
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     * @deprecated Prefer {@link TestingUtils#readObject(String, String, String, TypeRef)}.
     *             Subject to removal in the next major version 2.0.0.
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    default <T> T readObject(String testCase, ArtifactType artifactType, String artifactName, TypeRef<T> returnObjectType) {
        return readObject(testCase, artifactType.toString(), artifactName, returnObjectType);
    }

    /**
     * <p>
     *     The full method for reading input and expected test resources as objects.
     * </p>
     * <p>
     *     Prefer using the more streamlined variants:
     * </p>
     * <ul>
     *     <li>{@link TestingUtils#readInputObject(String, TypeRef)}</li>
     *     <li>{@link TestingUtils#readInputObject(String, String, TypeRef)}</li>
     *     <li>{@link TestingUtils#readExpectedObject(String, TypeRef)}</li>
     *     <li>{@link TestingUtils#readExpectedObject(String, String, TypeRef)}</li>
     * </ul>
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type (defaults in {@link DefaultArtifactType})
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    <T> T readObject(String testCase, String artifactType, String artifactName, TypeRef<T> returnObjectType);

    /**
     * <p>
     *     Convert JSON content to the specified type.
     * </p>
     * @param jsonContent JSON string to convert
     * @param returnObjectType the desired target type
     * @return the mapped object
     * @param <T> the desired target type
     */
    <T> T jsonToObject(String jsonContent, Class<T> returnObjectType);

    /**
     * <p>
     *     Convert JSON content to the specified type.
     * </p>
     * @param jsonContent JSON string to convert
     * @param returnObjectType the desired target type
     * @return the mapped object
     * @param <T> the desired target type
     */
    <T> T jsonToObject(String jsonContent, TypeRef<T> returnObjectType);

    /**
     * <p>
     *     Convert an object to raw JSON.
     * </p>
     * @param object JSON string to convert
     * @return the raw JSON string
     */
    String objectToJson(Object object);

    /**
     * <p>
     *     Reads an input JSON test resource as a string.
     * </p>
     * <p>
     *     Expects the resource to have the same name as the given type's simple name. For example, specifying
     *     {@code TestRequest.class} as the type would read the resource from
     *     {@code {suite}/{testCase}/input/TestRequest.json}.
     * </p>
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return String representation of the read resource
     */
    default String readInputString(String testCase, Class<?> returnObjectType) {
        return readInputString(testCase, artifactFileName(returnObjectType));
    }

    /**
     * <p>
     *     Reads an input test resource as a string.
     * </p>
     * <p>
     *     Unlike the variants where the {@code artifactName} argument is omitted, this variant does not infer the
     *     test resource name from a type, but uses the {@code artifactName} directly.
     * </p>
     * <p>
     *     For example, calling this method with an artifactName of {@code TestRequest.json},
     *     the test resource would be read from {@code {suite}/{testCase}/input/TestRequest.json}, returned
     *     as a string.
     * </p>
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @return String representation of the read resource
     */
    default String readInputString(String testCase, String artifactName) {
        return readString(testCase, DefaultArtifactType.INPUT, artifactName);
    }

    /**
     * <p>
     *     Reads an expected JSON test resource as a string.
     * </p>
     * <p>
     *     Expects the resource to have the same name as the given type's simple name. For example, specifying
     *     {@code TestRequest.class} as the type would read the resource from
     *     {@code {suite}/{testCase}/expected/TestRequest.json}.
     * </p>
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return String representation of the read resource
     */
    default String readExpectedString(String testCase, Class<?> returnObjectType) {
        return readExpectedString(testCase, artifactFileName(returnObjectType));
    }

    /**
     * <p>
     *     Reads an expected test resource as a string.
     * </p>
     * <p>
     *     Unlike the variants where the {@code artifactName} argument is omitted, this variant does not infer the
     *     test resource name from a type, but uses the {@code artifactName} directly.
     * </p>
     * <p>
     *     For example, calling this method with an artifactName of {@code TestRequest.json},
     *     the test resource would be read from {@code {suite}/{testCase}/expected/TestRequest.json}, returned
     *     as a string.
     * </p>
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @return String representation of the read resource
     */
    default String readExpectedString(String testCase, String artifactName) {
        return readString(testCase, DefaultArtifactType.EXPECTED, artifactName);
    }

    /**
     * <p>
     *     The full method for reading input and expected test resources as strings.
     * </p>
     * <p>
     *     Prefer using the more streamlined variants:
     * </p>
     * <ul>
     *     <li>{@link TestingUtils#readInputString(String, Class)}</li>
     *     <li>{@link TestingUtils#readInputString(String, String)}</li>
     *     <li>{@link TestingUtils#readExpectedString(String, Class)}</li>
     *     <li>{@link TestingUtils#readExpectedString(String, String)}</li>
     * </ul>
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type
     * @param artifactName the filename of the artifact to read
     * @return String representation of the read resource
     * @deprecated Prefer {@link TestingUtils#readString(String, String, String)}.
     *             Subject to removal in the next major version 2.0.0.
     */
    @SneakyThrows
    @Deprecated(since = "1.4.0", forRemoval = true)
    default String readString(String testCase, ArtifactType artifactType, String artifactName) {
        Path jsonPath = getArtifactPath(getSuite(), testCase, artifactType.toString(), artifactName);
        return new String(Files.readAllBytes(jsonPath));
    }

    /**
     * <p>
     *     The full method for reading input and expected test resources as strings.
     * </p>
     * <p>
     *     Prefer using the more streamlined variants:
     * </p>
     * <ul>
     *     <li>{@link TestingUtils#readInputString(String, Class)}</li>
     *     <li>{@link TestingUtils#readInputString(String, String)}</li>
     *     <li>{@link TestingUtils#readExpectedString(String, Class)}</li>
     *     <li>{@link TestingUtils#readExpectedString(String, String)}</li>
     * </ul>
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type (defaults in {@link DefaultArtifactType})
     * @param artifactName the filename of the artifact to read
     * @return String representation of the read resource
     */
    @SneakyThrows
    default String readString(String testCase, String artifactType, String artifactName) {
        Path jsonPath = getArtifactPath(getSuite(), testCase, artifactType, artifactName);
        return new String(Files.readAllBytes(jsonPath));
    }

    /**
     * <p>
     *     Asserts that the actualObject argument is strictly equal to its corresponding test resource found in expected
     *     resources. Infers the expected resource name from the type of the supplied object.
     * </p>
     * <p>
     *     For example, calling this method with an actualObject of type {@code TestRequest} would compare the given
     *     actualObject to the test resource loaded from {@code {suite}/{testCase}/expected/TestRequest.json}.
     * </p>
     * @param testCase the test case, used in the path
     * @param actualObject the actual object to compare to an expected resource
     */
    default void assertObject(String testCase, Object actualObject) {
        assertObject(testCase, artifactFileName(actualObject.getClass()), actualObject);
    }

    /**
     * <p>
     *     Asserts that the actualObject argument is strictly equal to a test resource found in expected resources.
     *     Unlike some other variants, this method does not infer the expected resource name from a type, but reads
     *     {@code expectedArtifactName} directly.
     * </p>
     * <p>
     *     For example, calling this method with an expectedArtifactName {@code TestRequest.json} would compare the
     *     given actualObject to the test resource loaded from {@code {suite}/{testCase}/expected/TestRequest.json}.
     * </p>
     * @param testCase the test case, used in the path
     * @param expectedArtifactName the filename of the expected artifact
     * @param actualObject the actual object to compare to an expected resource
     */
    default void assertObject(String testCase, String expectedArtifactName, Object actualObject) {
        assertObject(testCase, expectedArtifactName, actualObject, JSONCompareMode.NON_EXTENSIBLE);
    }

    /**
     * <p>
     *     The full method for assertions, where also strictness can be adjusted.
     * </p>
     * <p>
     *     Asserts that the actualObject argument is equal to a test resource found in expected resources.
     *     Unlike some other variants, this method does not infer the expected resource name from a type, but reads
     *     {@code expectedArtifactName} directly.
     * </p>
     * <p>
     *     For example, calling this method with an expectedArtifactName {@code TestRequest.json} would compare the
     *     given actualObject to the test resource loaded from {@code {suite}/{testCase}/expected/TestRequest.json}.
     * </p>
     * @param testCase the test case, used in the path
     * @param expectedArtifactName the filename of the expected artifact
     * @param actualObject the actual object to compare to an expected resource
     * @param mode to adjust comparison mode
     */
    @SneakyThrows
    default void assertObject(String testCase, String expectedArtifactName, Object actualObject, JSONCompareMode mode) {
        String actualJson = objectToJson(actualObject);
        String expectedJson = readString(testCase, DefaultArtifactType.EXPECTED, expectedArtifactName);
        JSONAssert.assertEquals(expectedJson, actualJson, mode);
    }

    /**
     * <p>
     *     Retrieve the suite value of this instance.
     * </p>
     * <p>
     *     When using {@link WithTestingUtils}, the suite value is automatically set.
     * </p>
     * @return the full test suite name / path.
     * @throws IllegalStateException If the current suite value is null or blank.
     */
    String getSuite() throws IllegalStateException;

    /**
     * <p>
     *     Set the suite value of this instance.
     * </p>
     * <p>
     *     When using {@link WithTestingUtils}, the suite value is automatically set, with no need to
     *     manually call this method.
     * </p>
     * @param suite the new value for suite
     */
    void setSuite(String suite);

    /**
     * <p>
     *     Return the default filename for the given class.
     * </p>
     * <p>
     *     For example, passing <code>SomeObject.class</code> would return <code>SomeObject.json</code>.
     * </p>
     * @param type the class to return a default filename for
     * @return The resolved filename
     */
    default String artifactFileName(Class<?> type) {
        return type.getSimpleName() + ".json";
    }

    /**
     * <p>
     *     Return the default filename for the given class.
     * </p>
     * <p>
     *     For example, passing {@code new TypeRef<SomeObject<SomeChild>>() {}} would return <code>SomeObject.json</code>.
     * </p>
     * @param type the class to return a default filename for
     * @return The resolved filename
     */
    default String artifactFileName(TypeRef<?> type) {
        return type.getSimpleName() + ".json";
    }

    /**
     * <p>
     *     Get a {@link Path} pointing to the given test artifact.
     * </p>
     * @param suite The test suite
     * @param testCase The test case
     * @param artifactType The artifact type
     * @param artifactName The artifact name
     * @return A {@link Path} pointing to the artifact resolved from the input parameters
     * @deprecated Prefer {@link TestingUtils#getArtifactPath(String, String, String, String)}.
     *             Subject to removal in the next major version 2.0.0.
     */
    @SneakyThrows
    @Deprecated(since = "1.4.0", forRemoval = true)
    default Path getArtifactPath(String suite, String testCase, ArtifactType artifactType, String artifactName) {
        return getArtifactPath(suite, testCase, artifactType.toString(), artifactName);
    }

    /**
     * <p>
     *     Get a {@link Path} pointing to the given test artifact.
     * </p>
     * @param suite The test suite
     * @param testCase The test case
     * @param artifactType The artifact type (defaults in {@link DefaultArtifactType})
     * @param artifactName The artifact name
     * @return A {@link Path} pointing to the artifact resolved from the input parameters
     */
    @SneakyThrows
    default Path getArtifactPath(String suite, String testCase, String artifactType, String artifactName) {
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(suite)).toURI())
            .resolve(testCase)
            .resolve(artifactType)
            .resolve(artifactName);
    }
}
