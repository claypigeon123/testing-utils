package com.purepigeon.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.extension.TestingUtilsExtension;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * <p>
 *     Provides various testing utilities, including:
 *     <ul>
 *         <li>Loading input / expected test resources as objects or strings in an organized fashion</li>
 *         <li>Asserting on test results using expected artifacts from test resources</li>
 *         <li>Converting JSON strings to objects</li>
 *     </ul>
 * </p>
 */
@TestComponent
@RequiredArgsConstructor
public class TestingUtils {
    @NonNull
    private final ObjectMapper objectMapper;

    @Nullable
    private String suite;

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
    public <T> T readInputObject(String testCase, Class<T> returnObjectType) {
        return readInputObject(getSuite(), testCase, returnObjectType);
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
    public <T> T readExpectedObject(String testCase, Class<T> returnObjectType) {
        return readExpectedObject(getSuite(), testCase, returnObjectType);
    }

    /**
     * <p>
     *     Reads an input JSON test resource as the specified type. 
     * </p>
     * <p>
     *     Expects the resource to have the same name as the given type's simple name. For example, specifying
     *     {@code TestRequest.class} as the type would read the resource from
     *     {@code {suite}/{testCase}/input/TestRequest.json}.
     * </p>
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    public <T> T readInputObject(String suite, String testCase, Class<T> returnObjectType) {
        return readInputObject(suite, testCase, artifactFileName(returnObjectType), returnObjectType);
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    public <T> T readExpectedObject(String suite, String testCase, Class<T> returnObjectType) {
        return readExpectedObject(suite, testCase, artifactFileName(returnObjectType), returnObjectType);
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    public <T> T readInputObject(String suite, String testCase, String artifactName, Class<T> returnObjectType) {
        return readObject(suite, testCase, ArtifactType.INPUT, artifactName, returnObjectType);
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    public <T> T readExpectedObject(String suite, String testCase, String artifactName, Class<T> returnObjectType) {
        return readObject(suite, testCase, ArtifactType.EXPECTED, artifactName, returnObjectType);
    }

    /**
     * <p>
     *     The full method for reading input and expected test resources as objects.
     * </p>
     * <p>
     *     Prefer using the more streamlined variants:
     *     <ul>
     *         <li>{@link TestingUtils#readInputObject(String, Class)}</li>
     *         <li>{@link TestingUtils#readInputObject(String, String, Class)}</li>
     *         <li>{@link TestingUtils#readInputObject(String, String, String, Class)}</li>
     *         <li>{@link TestingUtils#readExpectedObject(String, Class)}</li>
     *         <li>{@link TestingUtils#readExpectedObject(String, String, Class)}</li>
     *         <li>{@link TestingUtils#readExpectedObject(String, String, String, Class)}</li>
     *     </ul>
     * </p>
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type
     * @param artifactName the filename of the artifact to read
     * @param returnObjectType the desired type
     * @return Object, mapped from the read resource
     * @param <T> The desired type
     */
    @SneakyThrows
    public <T> T readObject(String suite, String testCase, ArtifactType artifactType, String artifactName, Class<T> returnObjectType) {
        Path jsonPath = getClassPathFilePath(suite, testCase, artifactType, artifactName);
        return objectMapper.readValue(new File(jsonPath.toUri()), returnObjectType);
    }

    /**
     * <p>
     *     Convert JSON content to the specified type.
     * </p>
     * @param jsonContent JSON string to convert
     * @param returnObjectType the desired target type
     * @return the mapped object
     * @param <T> the desired target type
     */
    @SneakyThrows
    public <T> T jsonToObject(String jsonContent, Class<T> returnObjectType) {
        return objectMapper.readValue(jsonContent, returnObjectType);
    }

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
    public String readInputString(String testCase, Class<?> returnObjectType) {
        return readInputString(getSuite(), testCase, returnObjectType);
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
    public String readExpectedString(String testCase, Class<?> returnObjectType) {
        return readExpectedString(getSuite(), testCase, returnObjectType);
    }

    /**
     * <p>
     *     Reads an input JSON test resource as a string. 
     * </p>
     * <p>
     *     Expects the resource to have the same name as the given type's simple name. For example, specifying
     *     {@code TestRequest.class} as the type would read the resource from
     *     {@code {suite}/{testCase}/input/TestRequest.json}.
     * </p>
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return String representation of the read resource
     */
    public String readInputString(String suite, String testCase, Class<?> returnObjectType) {
        return readInputString(suite, testCase, artifactFileName(returnObjectType));
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param returnObjectType the desired type
     * @return String representation of the read resource
     */
    public String readExpectedString(String suite, String testCase, Class<?> returnObjectType) {
        return readExpectedString(suite, testCase, artifactFileName(returnObjectType));
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @return String representation of the read resource
     */
    public String readInputString(String suite, String testCase, String artifactName) {
        return readString(suite, testCase, ArtifactType.INPUT, artifactName);
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param artifactName the filename of the artifact to read
     * @return String representation of the read resource
     */
    public String readExpectedString(String suite, String testCase, String artifactName) {
        return readString(suite, testCase, ArtifactType.EXPECTED, artifactName);
    }

    /**
     * <p>
     *     The full method for reading input and expected test resources as strings.
     * </p>
     * <p>
     *     Prefer using the more streamlined variants:
     *     <ul>
     *         <li>{@link TestingUtils#readInputString(String, Class)}</li>
     *         <li>{@link TestingUtils#readInputString(String, String, Class)}</li>
     *         <li>{@link TestingUtils#readInputString(String, String, String)}</li>
     *         <li>{@link TestingUtils#readExpectedString(String, Class)}</li>
     *         <li>{@link TestingUtils#readExpectedString(String, String, Class)}</li>
     *         <li>{@link TestingUtils#readExpectedString(String, String, String)}</li>
     *     </ul>
     * </p>
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param artifactType the artifact type
     * @param artifactName the filename of the artifact to read
     * @return String representation of the read resource
     */
    @SneakyThrows
    public String readString(String suite, String testCase, ArtifactType artifactType, String artifactName) {
        Path jsonPath = getClassPathFilePath(suite, testCase, artifactType, artifactName);
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
    public void assertObject(String testCase, Object actualObject) {
        assertObject(getSuite(), testCase, actualObject);
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param actualObject the actual object to compare to an expected resource
     */
    public void assertObject(String suite, String testCase, Object actualObject) {
        assertObject(suite, testCase, artifactFileName(actualObject.getClass()), actualObject, true);
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
     * @param suite the test suite, used in the path
     * @param testCase the test case, used in the path
     * @param expectedArtifactName the filename of the expected artifact
     * @param actualObject the actual object to compare to an expected resource
     */
    public void assertObject(String suite, String testCase, String expectedArtifactName, Object actualObject) {
        assertObject(suite, testCase, expectedArtifactName, actualObject, true);
    }

    /**
     * <p>
     *     The full method for assertions, where strictness can be adjusted. Specifying {@code true} uses
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
     */
    @SneakyThrows
    public void assertObject(String suite, String testCase, String expectedArtifactName, Object actualObject, boolean strict) {
        String actualJson = objectMapper.writeValueAsString(actualObject);
        String expectedJson = readString(suite, testCase, ArtifactType.EXPECTED, expectedArtifactName);
        JSONAssert.assertEquals(expectedJson, actualJson, strict ? JSONCompareMode.NON_EXTENSIBLE : JSONCompareMode.LENIENT);
    }

    private Path getClassPathFilePath(String suite, String testCase, ArtifactType artifactType, String artifactName) {
        Path classPathDirPath = getClassPathDirPath(suite, testCase, artifactType);
        return classPathDirPath.resolve(artifactName);
    }

    @SneakyThrows
    private Path getClassPathDirPath(String suite, String testCase, ArtifactType artifactType) {
        URL suiteResource = Objects.requireNonNull(getClass().getClassLoader().getResource(suite));
        Path suiteBasePath = Paths.get(suiteResource.toURI());

        Path testDirRelativePath = Paths.get(testCase, artifactType.toString());
        return suiteBasePath.resolve(testDirRelativePath);
    }

    private String artifactFileName(Class<?> returnObjectType) {
        return returnObjectType.getSimpleName() + ".json";
    }

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
    public String getSuite() {
        if (!StringUtils.hasText(suite)) {
            throw new IllegalStateException("Suite property is not set");
        }
        return suite;
    }

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
    @Autowired(required = false)
    public void setSuite(@Nullable String suite) {
        this.suite = suite;
    }

    /**
     * <p>
     *     Artifact type, used to build a path to a given test resource.
     * </p>
     */
    public enum ArtifactType {
        /**
         * <p>
         *     Means that the resource will be looked for in the input subfolder of the given suite and test case.
         * </p>
         */
        INPUT,

        /**
         * <p>
         *     Means that the resource will be looked for in the expected subfolder of the given suite and test case.
         * </p>
         */
        EXPECTED;

        /**
         * <p>
         *     Overridden to return the lowercase value of the enum constant.
         * </p>
         * @return the lowercase value of the enum constant.
         */
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
