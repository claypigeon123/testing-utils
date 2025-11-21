package com.purepigeon.test.utils.mockwebserver.impl;

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

import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.TypeRef;
import com.purepigeon.test.utils.mockwebserver.MockWebServerSupport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import okhttp3.Headers;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class MockWebServerSupportImpl implements MockWebServerSupport {

    @NonNull
    private final TestingUtils testingUtils;

    private MockWebServer mockWebServer = new MockWebServer();

    @Override
    @SneakyThrows
    public void start() {
        start(0);
    }

    @Override
    @SneakyThrows
    public void start(int port) {
        mockWebServer.start(port);
    }

    @Override
    public void close() {
        mockWebServer.close();
        reset();
    }

    @Override
    public int port() {
        return mockWebServer.getPort();
    }

    @Override
    public void enqueueResource(String testCase, String artifactType, String artifactName, int status, Headers headers) {
        var resource = testingUtils.readString(testCase, artifactType, artifactName);

        mockWebServer.enqueue(new MockResponse.Builder()
            .code(status)
            .headers(headers)
            .body(resource)
            .build()
        );
    }

    @Override
    public RecordedRequest takeRequest(long timeoutMs) {
        return assertDoesNotThrow(() -> mockWebServer.takeRequest(timeoutMs, TimeUnit.MILLISECONDS));
    }

    @Override
    public MockWebServer unwrap() {
        return mockWebServer;
    }

    @Override
    public <T> String artifactFileName(Class<T> clazz) {
        return testingUtils.artifactFileName(clazz);
    }

    @Override
    public <T> String artifactFileName(TypeRef<T> typeRef) {
        return testingUtils.artifactFileName(typeRef);
    }

    @Override
    public <T> RecordedRequest assertRequest(String testCase, Class<T> clazz) {
        RecordedRequest recordedRequest = this.takeRequest();

        T actual = testingUtils.jsonToObject(getRequestBody(recordedRequest), clazz);
        testingUtils.assertObject(testCase, actual);

        return recordedRequest;
    }

    @Override
    public <T> RecordedRequest assertRequest(String testCase, TypeRef<T> typeRef) {
        RecordedRequest recordedRequest = this.takeRequest();

        T actual = testingUtils.jsonToObject(getRequestBody(recordedRequest), typeRef);
        testingUtils.assertObject(testCase, actual);

        return recordedRequest;
    }

    // --

    private String getRequestBody(RecordedRequest recordedRequest) {
        assertNotNull(recordedRequest.getBody());
        return recordedRequest.getBody().string(Charset.defaultCharset());
    }

    private void reset() {
        mockWebServer.close();
        mockWebServer = new MockWebServer();
    }
}
