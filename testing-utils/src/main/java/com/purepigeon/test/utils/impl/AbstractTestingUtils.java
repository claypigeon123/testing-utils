package com.purepigeon.test.utils.impl;

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

/**
 * <p>
 *     Abstract class that encapsulates 'suite' handling, which is likely common, regardless of implementation.
 * </p>
 * <p>
 *     Classes that want to implement the {@link TestingUtils} interface should extend this class instead.
 * </p>
 */
public abstract class AbstractTestingUtils implements TestingUtils {

    /**
     * <p>The current suite value.</p>
     */
    protected String suite;

    @Override
    public String getSuite() throws IllegalStateException {
        if (suite == null || suite.isBlank()) {
            throw new IllegalStateException("Suite property is not set");
        }
        return suite;
    }

    @Override
    public void setSuite(String suite) {
        this.suite = suite;
    }
}
