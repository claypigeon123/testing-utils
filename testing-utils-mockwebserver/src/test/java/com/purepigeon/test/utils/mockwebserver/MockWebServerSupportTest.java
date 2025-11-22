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
import com.purepigeon.test.utils.annotation.TestCase;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.mockwebserver.annotation.EnqueueResponse;
import com.purepigeon.test.utils.mockwebserver.annotation.MockWebServerlessTest;
import com.purepigeon.test.utils.mockwebserver.annotation.WithMockWebServer;
import com.purepigeon.test.utils.mockwebserver.test.*;
import okhttp3.Headers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@WithTestingUtils
@WithMockWebServer
@SpringBootTest(classes = { TestApp.class, TestConfig.class})
class MockWebServerSupportTest {

    private static final String URI = "http://localhost:{port}";
    private static final String TEST_RESPONSE_JSON = "TestResponse.json";
    private static final String TEST_ARTIFACT_TYPE = "test";
    private static final String HEADER_NAME_TEST = "X-Test";

    @Autowired
    private TestingUtils testingUtils;

    @Autowired
    private MockWebServerSupport mockWebServerSupport;

    @Autowired
    private RestClient restClient;

    @Test
    void testOptedIn() {
        // expect
        assertTrue(mockWebServerSupport.unwrap().getStarted());
        assertNotEquals(0, assertDoesNotThrow(() -> mockWebServerSupport.port()));
    }

    @Test
    @MockWebServerlessTest
    void testOptedOut() {
        // expect
        assertFalse(mockWebServerSupport.unwrap().getStarted());
        assertThrows(IllegalStateException.class, () -> mockWebServerSupport.port());
    }

    @Test
    @MockWebServerlessTest
    void testManualUse() {
        // when
        mockWebServerSupport.start();

        // then
        assertTrue(mockWebServerSupport.unwrap().getStarted());

        // cleanup
        mockWebServerSupport.stop();
    }

    @Test
    @TestCase("plain")
    void enqueueInputResource_class(String testCase) {
        // when
        mockWebServerSupport.enqueueInputResource(testCase, TestResponse.class);

        // then
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    @EnqueueResponse(TestResponse.class)
    void enqueueInputResource_viaAnnotation_class(String testCase) {
        // expect
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    @EnqueueResponse(TestResponse.class)
    @EnqueueResponse(TestResponse.class)
    void enqueueInputResource_multipleViaAnnotation_class(String testCase) {
        // expect
        performGet(testCase, TestResponse.class);
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("generic")
    void enqueueInputResource_typeRef(String testCase) {
        // when
        mockWebServerSupport.enqueueInputResource(testCase, new TypeRef<GenericTestResponse<TestResponse>>() {});

        // then
        performGet(testCase, GenericTestResponse.class);
    }

    @Test
    @TestCase("plain")
    void enqueueInputResource_named(String testCase) {
        // when
        mockWebServerSupport.enqueueInputResource(testCase, TEST_RESPONSE_JSON);

        // then
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    @EnqueueResponse(artifactName = TEST_RESPONSE_JSON)
    void enqueueInputResource_viaAnnotation_named(String testCase) {
        // expect
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    @EnqueueResponse(artifactName = TEST_RESPONSE_JSON)
    @EnqueueResponse(artifactName = TEST_RESPONSE_JSON)
    void enqueueInputResource_multipleViaAnnotation_named(String testCase) {
        // expect
        performGet(testCase, TestResponse.class);
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    void enqueueExpectedResource_class(String testCase) {
        // when
        mockWebServerSupport.enqueueExpectedResource(testCase, TestResponse.class);

        // then
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    @EnqueueResponse(value = TestResponse.class, artifactType = DefaultArtifactType.EXPECTED)
    void enqueueExpectedResource_viaAnnotation_class(String testCase) {
        // expect
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    @EnqueueResponse(value = TestResponse.class, artifactType = DefaultArtifactType.EXPECTED)
    @EnqueueResponse(value = TestResponse.class, artifactType = DefaultArtifactType.EXPECTED)
    void enqueueExpectedResource_multipleViaAnnotation_class(String testCase) {
        // expect
        performGet(testCase, TestResponse.class);
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("generic")
    void enqueueExpectedResource_typeRef(String testCase) {
        // when
        mockWebServerSupport.enqueueExpectedResource(testCase, new TypeRef<GenericTestResponse<TestResponse>>() {});

        // then
        performGet(testCase, GenericTestResponse.class);
    }

    @Test
    @TestCase("plain")
    void enqueueExpectedResource_named(String testCase) {
        // when
        mockWebServerSupport.enqueueExpectedResource(testCase, TEST_RESPONSE_JSON);

        // then
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    @EnqueueResponse(artifactName = TEST_RESPONSE_JSON, artifactType = DefaultArtifactType.EXPECTED)
    void enqueueExpectedResource_viaAnnotation_named(String testCase) {
        // expect
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    @EnqueueResponse(artifactName = TEST_RESPONSE_JSON, artifactType = DefaultArtifactType.EXPECTED)
    @EnqueueResponse(artifactName = TEST_RESPONSE_JSON, artifactType = DefaultArtifactType.EXPECTED)
    void enqueueExpectedResource_multipleViaAnnotation_named(String testCase) {
        // expect
        performGet(testCase, TestResponse.class);
        performGet(testCase, TestResponse.class);
    }

    @Test
    @TestCase("plain")
    void enqueueResource(String testCase) {
        // when
        mockWebServerSupport.enqueueResource(
            testCase,
            TEST_ARTIFACT_TYPE,
            TEST_RESPONSE_JSON,
            HttpStatus.ACCEPTED.value(),
            Headers.of(
                HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE,
                HEADER_NAME_TEST, testCase
            )
        );

        // then
        performGet(
            testCase,
            TestResponse.class,
            HttpStatus.ACCEPTED,
            headers -> {
                assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
                assertEquals(List.of(testCase), headers.get(HEADER_NAME_TEST));
            }
        );
    }

    @Test
    @TestCase("plain")
    void takeRequest(String testCase) {
        // given
        var request = testingUtils.readInputObject(testCase, TestRequest.class);
        mockWebServerSupport.enqueueExpectedResource(testCase, TestResponse.class);

        performPost(testCase, TestResponse.class, request);

        // when
        var recordedRequest = mockWebServerSupport.takeRequest();

        // then
        assertNotNull(recordedRequest);
    }

    @Test
    void artifactFileName_class() {
        var clazz = TestResponse.class;
        assertEquals(testingUtils.artifactFileName(clazz), mockWebServerSupport.artifactFileName(clazz));
    }

    @Test
    void artifactFileName_typeRef() {
        var typeRef = new TypeRef<GenericTestResponse<TestResponse>>() {};
        assertEquals(testingUtils.artifactFileName(typeRef), mockWebServerSupport.artifactFileName(typeRef));
    }

    @Test
    @TestCase("plain")
    void assertRequest_class(String testCase) {
        // given
        var request = testingUtils.readInputObject(testCase, TestRequest.class);
        mockWebServerSupport.enqueueExpectedResource(testCase, TestResponse.class);

        performPost(testCase, TestResponse.class, request);

        // when
        assertDoesNotThrow(() -> mockWebServerSupport.assertRequest(testCase, TestRequest.class));
    }

    @Test
    @TestCase("generic")
    void assertRequest_generic(String testCase) {
        // given
        var request = testingUtils.readInputObject(testCase, new TypeRef<GenericTestRequest<TestRequest>>() {});
        mockWebServerSupport.enqueueExpectedResource(testCase, new TypeRef<GenericTestResponse<TestResponse>>() {});

        performPost(testCase, GenericTestResponse.class, request);

        // when
        assertDoesNotThrow(() -> mockWebServerSupport.assertRequest(testCase, new TypeRef<GenericTestRequest<TestRequest>>() {}));
    }

    // --

    private <T> void performGet(String testCase, Class<T> clazz) {
        performGet(
            testCase,
            clazz,
            HttpStatus.OK,
            headers -> assertEquals(MediaType.APPLICATION_JSON, headers.getContentType())
        );
    }

    private <T> void performGet(String testCase, Class<T> responseClass, HttpStatus expectedStatus, Consumer<HttpHeaders> assertHeaders) {
        performTest(
            testCase,
            responseClass,
            HttpMethod.GET,
            null,
            expectedStatus,
            assertHeaders
        );
    }

    private <T> void performPost(String testCase, Class<T> responseClass, Object body) {
        performTest(
            testCase,
            responseClass,
            HttpMethod.POST,
            body,
            HttpStatus.OK,
            headers -> assertEquals(MediaType.APPLICATION_JSON, headers.getContentType())
        );
    }

    private <T> void performTest(String testCase, Class<T> responseClass, HttpMethod method, Object body, HttpStatus expectedStatus, Consumer<HttpHeaders> assertHeaders) {
        var builder = restClient.method(method)
            .uri(URI, mockWebServerSupport.port());

        if (body != null) {
            builder = builder.body(body);
        }

        var response = builder.retrieve().toEntity(responseClass);

        testingUtils.assertObject(testCase, response.getBody());
        assertEquals(expectedStatus, response.getStatusCode());
        assertHeaders.accept(response.getHeaders());
    }
}
