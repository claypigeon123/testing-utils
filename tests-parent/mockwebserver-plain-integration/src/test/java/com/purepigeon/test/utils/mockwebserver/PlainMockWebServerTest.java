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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.annotation.WithTestingUtils;
import com.purepigeon.test.utils.impl.jackson.JacksonTestingUtils;
import com.purepigeon.test.utils.mockwebserver.annotation.WithMockWebServer;
import com.purepigeon.test.utils.mockwebserver.model.RequestBody;
import com.purepigeon.test.utils.mockwebserver.model.ResponseBody;
import lombok.SneakyThrows;
import mockwebserver3.RecordedRequest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithTestingUtils
@WithMockWebServer
class PlainMockWebServerTest {

    private static final String URI_TEMPLATE = "http://localhost:%d";
    private static final String METHOD_POST = "POST";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_ACCEPT = "Accept";
    private static final String APPLICATION_JSON_VALUE = "application/json";

    private final TestingUtils testingUtils = new JacksonTestingUtils(new ObjectMapper());

    private final MockWebServerSupport mockWebServer = MockWebServerSupport.createDefault(testingUtils);

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void testUsage(String testCase) {
        // given
        mockWebServer.enqueueExpectedResource(testCase, ResponseBody.class);

        // when
        var response = postTo(testCase, mockWebServer.port());

        // then
        testingUtils.assertObject(testCase, response);
        var recordedRequest = mockWebServer.assertRequest(testCase, RequestBody.class);
        assertMethodAndHeaders(recordedRequest);
    }

    // --

    @SneakyThrows
    private ResponseBody postTo(String testCase, int port) {
        var body = testingUtils.readExpectedString(testCase, RequestBody.class);

        var request = HttpRequest.newBuilder()
            .uri(URI.create(URI_TEMPLATE.formatted(port)))
            .header(HEADER_CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .header(HEADER_ACCEPT, APPLICATION_JSON_VALUE)
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return testingUtils.jsonToObject(response.body(), ResponseBody.class);
    }

    private void assertMethodAndHeaders(RecordedRequest request) {
        assertEquals(METHOD_POST, request.getMethod());

        assertEquals(APPLICATION_JSON_VALUE, request.getHeaders().get(HEADER_CONTENT_TYPE));
        assertEquals(APPLICATION_JSON_VALUE, request.getHeaders().get(HEADER_ACCEPT));
    }
}
