package com.purepigeon.test.utils.impl.simple;

/*-
 * #%L
 * Testing Utils
 * %%
 * Copyright (C) 2025 - 2026 Purepigeon
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

import com.purepigeon.test.utils.AbstractTestingUtilsTest;
import com.purepigeon.test.utils.TestingUtils;
import com.purepigeon.test.utils.test.TestData;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import tools.jackson.databind.ObjectMapper;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleTestingUtilsTest extends AbstractTestingUtilsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Autowired
    protected void setTestingUtils(@Qualifier("standaloneTestingUtils") TestingUtils testingUtils) {
        this.testingUtils = testingUtils;
    }

    @Test
    @Override
    protected void assertImpl() {
        assertInstanceOf(SimpleTestingUtils.class, testingUtils);
    }

    @Test
    @Override
    protected void objectToJson() {
        assertThrows(UnsupportedOperationException.class, super::objectToJson);
    }

    @Test
    @Override
    protected void assertObject_testCaseOnly(String testCase) {
        assertThrows(UnsupportedOperationException.class, () -> super.assertObject_testCaseOnly(testCase));
    }

    @Test
    @Override
    protected void assertObject_testCaseAndArtifactName(String testCase) {
        assertThrows(UnsupportedOperationException.class, () -> super.assertObject_testCaseAndArtifactName(testCase));
    }

    @Override
    @ParameterizedTest
    @CsvSource({
        "NON_EXTENSIBLE",
        "STRICT"
    })
    protected void assertObject_testCaseAndArtifactNameAndStrictness(String strictness, String testCase) {
        assertThrows(UnsupportedOperationException.class, () -> super.assertObject_testCaseAndArtifactNameAndStrictness(strictness, testCase));
    }

    @Test
    @Override
    protected void genericReadTest(String testCase) {
        assertThrows(UnsupportedOperationException.class, () -> super.genericReadTest(testCase));
    }

    @Test
    @Override
    protected void semiGenericReadTest(String testCase) {
        assertThrows(UnsupportedOperationException.class, () -> super.semiGenericReadTest(testCase));
    }

    // --

    @Override
    protected void performReadTest(Supplier<TestData> resultSupplier) {
        assertThrows(UnsupportedOperationException.class, resultSupplier::get);
    }

    @Override
    @SneakyThrows
    protected void performRawReadTest(Supplier<String> resultSupplier) {
        // given
        var expected = objectMapper.writeValueAsString(TestData.create());

        // when
        var actual = resultSupplier.get();

        // then
        JSONAssert.assertEquals(expected, actual, true);
    }
}
