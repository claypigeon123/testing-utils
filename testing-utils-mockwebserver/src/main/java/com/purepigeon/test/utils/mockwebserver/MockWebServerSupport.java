package com.purepigeon.test.utils.mockwebserver;

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
import com.purepigeon.test.utils.TypeRef;
import com.purepigeon.test.utils.mockwebserver.annotation.WithMockWebServer;
import com.purepigeon.test.utils.mockwebserver.impl.MockWebServerSupportImpl;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import okhttp3.Headers;

/**
 * <p> This interface is a wrapper for okhttp3 {@link MockWebServer}. </p>
 * <p>
 *     It provides convenience methods similar to the {@link TestingUtils} interface for enqueuing responses and
 *     asserting on received requests. The default implementation - {@link MockWebServerSupportImpl} - integrates with
 *     {@link TestingUtils}.
 * </p>
 * <p> {@link MockWebServerSupport#unwrap()} can be used to access the underlying mock web server. </p>
 * @see MockWebServerSupportImpl
 * @see WithMockWebServer
 */
public interface MockWebServerSupport {

    /**
     * <p> Convenience factory method to create an instance with the default implementation. </p>
     * @param testingUtils the testing utils instance to use
     * @return Instance with default implementation
     */
    static MockWebServerSupport createDefault(TestingUtils testingUtils) {
        return new MockWebServerSupportImpl(testingUtils);
    }

    /**
     * <p> Start the mock web server on a random unused port. </p>
     */
    void start();

    /**
     * Start the mock web server on the specified port. If 0 is provided, a random unused port will be selected.
     * @param port the port to listen on
     */
    void start(int port);

    /**
     * <p> Stop the mock web server and re-initialize it in an unstarted state. </p>
     */
    void stop();

    /**
     * <p> Get the port that the started mock web server is listening on. </p>
     * @return Actual port
     */
    int port();

    /**
     * <p> Enqueue a response based on an input resource whose name will be resolved from {@code clazz}. </p>
     * <p> Response status will be {@code 200}, with one header {@code Content-Type: application/json} </p>
     * @param testCase the test case
     * @param clazz the resource class
     * @param <T> resource type
     * @see TestingUtils#readInputObject(String, Class)
     */
    default <T> void enqueueInputResource(String testCase, Class<T> clazz) {
        enqueueInputResource(testCase, artifactFileName(clazz));
    }

    /**
     * <p> Enqueue a response based on an input resource whose name will be resolved from {@code typeRef}. </p>
     * <p> Response status will be {@code 200}, with one header {@code Content-Type: application/json} </p>
     * @param testCase the test case
     * @param typeRef the resource type reference
     * @param <T> resource type
     * @see TestingUtils#readInputObject(String, TypeRef)
     */
    default <T> void enqueueInputResource(String testCase, TypeRef<T> typeRef) {
        enqueueInputResource(testCase, artifactFileName(typeRef));
    }

    /**
     * <p> Enqueue a response based on an input resource with exact filename of {@code artifactName}. </p>
     * <p> Response status will be {@code 200}, with one header {@code Content-Type: application/json} </p>
     * @param testCase the test case
     * @param artifactName the filename of the resource
     * @see TestingUtils#readInputString(String, String)
     */
    default void enqueueInputResource(String testCase, String artifactName) {
        enqueueResource(testCase, DefaultArtifactType.INPUT, artifactName, 200, Headers.of("Content-Type", "application/json"));
    }

    /**
     * <p> Enqueue a response based on an expected resource whose name will be resolved from {@code clazz}. </p>
     * <p> Response status will be {@code 200}, with one header {@code Content-Type: application/json} </p>
     * @param testCase the test case
     * @param clazz the resource class
     * @param <T> resource type
     * @see TestingUtils#readExpectedObject(String, Class)
     */
    default <T> void enqueueExpectedResource(String testCase, Class<T> clazz) {
        enqueueExpectedResource(testCase, artifactFileName(clazz));
    }

    /**
     * <p> Enqueue a response based on an expected resource whose name will be resolved from {@code typeRef}. </p>
     * <p> Response status will be {@code 200}, with one header {@code Content-Type: application/json} </p>
     * @param testCase the test case
     * @param typeRef the resource class
     * @param <T> resource type
     * @see TestingUtils#readExpectedObject(String, TypeRef)
     */
    default <T> void enqueueExpectedResource(String testCase, TypeRef<T> typeRef) {
        enqueueExpectedResource(testCase, artifactFileName(typeRef));
    }

    /**
     * <p> Enqueue a response based on an expected resource with exact filename of {@code artifactName}. </p>
     * <p> Response status will be {@code 200}, with one header {@code Content-Type: application/json} </p>
     * @param testCase the test case
     * @param artifactName the filename of the resource
     * @see TestingUtils#readExpectedString(String, String)
     */
    default void enqueueExpectedResource(String testCase, String artifactName) {
        enqueueResource(testCase, DefaultArtifactType.EXPECTED, artifactName, 200, Headers.of("Content-Type", "application/json"));
    }

    /**
     * <p> The full method for enqueuing responses. </p>
     * <p> Prefer using the more streamlined variants, if possible: </p>
     * <ul>
     *     <li>{@link MockWebServerSupport#enqueueInputResource(String, Class)}</li>
     *     <li>{@link MockWebServerSupport#enqueueInputResource(String, TypeRef)}</li>
     *     <li>{@link MockWebServerSupport#enqueueInputResource(String, String)}</li>
     *     <li>{@link MockWebServerSupport#enqueueExpectedResource(String, Class)}</li>
     *     <li>{@link MockWebServerSupport#enqueueExpectedResource(String, TypeRef)}</li>
     *     <li>{@link MockWebServerSupport#enqueueExpectedResource(String, String)}</li>
     * </ul>
     * @param testCase the test case
     * @param artifactType the artifact type
     * @param artifactName the artifact filename
     * @param status the response status
     * @param headers the response headers
     */
    void enqueueResource(String testCase, String artifactType, String artifactName, int status, Headers headers);

    /**
     * <p> Blocks until a recorded request is available to return, for a maximum of 60 seconds. </p>
     * @return The next recorded request in the queue
     */
    default RecordedRequest takeRequest() {
        return takeRequest(60_000L);
    }

    /**
     * <p>
     *     Blocks until a recorded request is available to return, for a maximum of {@code timeoutMs} millis.
     *     If 0 is provided, this method can block indefinitely.
     * </p>
     * @param timeoutMs maximum time to wait for a request in millis
     * @return The next recorded request in the queue
     */
    RecordedRequest takeRequest(long timeoutMs);

    /**
     * <p> Provides access to the underlying mock web server instance. </p>
     * @return The wrapped mock web server
     */
    MockWebServer unwrap();

    /**
     * <p>
     *     Assert that a request has been recorded with a body matching an expected artifact loaded via
     *     {@code testCase} and {@code clazz}.
     * </p>
     * <p>
     *     After the body is asserted, this method returns the recorded request for potential further assertions.
     * </p>
     * @param testCase the test case
     * @param clazz the artifact class
     * @return the recorded request
     * @param <T> resource type
     */
    <T> RecordedRequest assertRequest(String testCase, Class<T> clazz);

    /**
     * <p>
     *     Assert that a request has been recorded with a body matching an expected artifact loaded via
     *     {@code testCase} and {@code typeRef}.
     * </p>
     * <p>
     *     After the body is asserted, this method returns the recorded request for potential further assertions.
     * </p>
     * @param testCase the test case
     * @param typeRef the artifact class
     * @return the recorded request
     * @param <T> resource type
     */
    <T> RecordedRequest assertRequest(String testCase, TypeRef<T> typeRef);

    /**
     * <p>
     *     Return the default filename for the given class.
     * </p>
     * @param clazz the class to return a default filename for
     * @return The resolved filename
     * @see TestingUtils#artifactFileName(Class)
     */
    <T> String artifactFileName(Class<T> clazz);

    /**
     * <p>
     *     Return the default filename for the given type reference.
     * </p>
     * @param typeRef the type reference to return a default filename for
     * @return The resolved filename
     * @see TestingUtils#artifactFileName(TypeRef)
     */
    <T> String artifactFileName(TypeRef<T> typeRef);
}
