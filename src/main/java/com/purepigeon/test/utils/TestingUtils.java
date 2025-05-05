package com.purepigeon.test.utils;

import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.extension.TestingUtilsExtension;
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
 *     <li>Loading input / expected test resources as objects or strings in an organized fashion</li>
 *     <li>Asserting on test results using expected artifacts from test resources</li>
 *     <li>Converting JSON strings to objects</li>
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
        return readObject(getSuite(), testCase, ArtifactType.INPUT, artifactName, returnObjectType);
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
        return readObject(getSuite(), testCase, ArtifactType.EXPECTED, artifactName, returnObjectType);
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, Class<T> returnObjectType);

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
        return readString(getSuite(), testCase, ArtifactType.INPUT, artifactName);
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
        return readString(getSuite(), testCase, ArtifactType.EXPECTED, artifactName);
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type
     * @param artifactName the filename of the artifact to read
     * @return String representation of the read resource
     */
    @SneakyThrows
    default String readString(String suite, String testCase, ArtifactType artifactType, String artifactName) {
        Path jsonPath = getArtifactPath(suite, testCase, artifactType, artifactName);
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
        assertObject(getSuite(), testCase, expectedArtifactName, actualObject, true);
    }

    /**
     * <p>
     *     The full method for assertions, where also strictness can be adjusted. Specifying {@code true} uses
     *     {@link JSONAssert}'s {@link JSONCompareMode#NON_EXTENSIBLE}, whereas {@code false} uses
     *     {@link JSONCompareMode#LENIENT}.
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param expectedArtifactName the filename of the expected artifact
     * @param actualObject the actual object to compare to an expected resource
     * @param strict to adjust comparison mode
     */
    void assertObject(String suite, String testCase, String expectedArtifactName, Object actualObject, boolean strict);

    /**
     * <p>
     *     Retrieve the suite value of this instance.
     * </p>
     * <p>
     *     When using {@link WithTestingUtils} or {@link TestingUtilsExtension} directly, the suite value is
     *     automatically set.
     * </p>
     * @return the full test suite name / path.
     */
    String getSuite();

    /**
     * <p>
     *     Set the suite value of this instance.
     * </p>
     * <p>
     *     When using {@link WithTestingUtils} or {@link TestingUtilsExtension} directly, the suite value is
     *     automatically set, with no need to manually call this method.
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
     *     Get a {@link Path} pointing to the given test artifact.
     * </p>
     * @param suite The test suite
     * @param testCase The test case
     * @param artifactType The artifact type
     * @param artifactName The artifact name
     * @return A {@link Path} pointing to the artifact resolved from the input parameters
     */
    @SneakyThrows
    default Path getArtifactPath(String suite, String testCase, ArtifactType artifactType, String artifactName) {
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(suite)).toURI())
            .resolve(testCase)
            .resolve(artifactType.toString())
            .resolve(artifactName);
    }
}
