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
import com.purepigeon.test.utils.mockwebserver.impl.MockWebServerSupportImpl;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import okhttp3.Headers;

public interface MockWebServerSupport extends AutoCloseable {

    static MockWebServerSupport createDefault(TestingUtils testingUtils) {
        return new MockWebServerSupportImpl(testingUtils);
    }

    void start();

    void start(int port);

    void close();

    int port();

    default <T> void enqueueInputResource(String testCase, Class<T> clazz) {
        enqueueInputResource(testCase, artifactFileName(clazz));
    }

    default <T> void enqueueInputResource(String testCase, TypeRef<T> typeRef) {
        enqueueInputResource(testCase, artifactFileName(typeRef));
    }

    default void enqueueInputResource(String testCase, String artifactName) {
        enqueueResource(testCase, DefaultArtifactType.INPUT, artifactName, 200, Headers.of("Content-Type", "application/json"));
    }

    default <T> void enqueueExpectedResource(String testCase, Class<T> clazz) {
        enqueueExpectedResource(testCase, artifactFileName(clazz));
    }

    default <T> void enqueueExpectedResource(String testCase, TypeRef<T> typeRef) {
        enqueueExpectedResource(testCase, artifactFileName(typeRef));
    }

    default void enqueueExpectedResource(String testCase, String artifactName) {
        enqueueResource(testCase, DefaultArtifactType.EXPECTED, artifactName, 200, Headers.of("Content-Type", "application/json"));
    }

    void enqueueResource(String testCase, String artifactType, String artifactName, int status, Headers headers);

    default RecordedRequest takeRequest() {
        return takeRequest(60_000L);
    }

    <T> RecordedRequest assertRequest(String testCase, Class<T> clazz);

    <T> RecordedRequest assertRequest(String testCase, TypeRef<T> typeRef);

    RecordedRequest takeRequest(long timeoutMs);

    MockWebServer unwrap();

    <T> String artifactFileName(Class<T> clazz);

    <T> String artifactFileName(TypeRef<T> typeRef);
}
