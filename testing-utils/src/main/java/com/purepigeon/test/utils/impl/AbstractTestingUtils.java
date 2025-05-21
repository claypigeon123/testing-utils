package com.purepigeon.test.utils.impl;

import com.purepigeon.test.utils.TestingUtils;

/**
 * <p>
 *     Abstract class that encapsulates some functionalities that are likely common, regardless of binding.
 * </p>
 * <p>
 *     Classes that want to implement the {@link TestingUtils} interface should extend this class instead.
 * </p>
 */
public abstract class AbstractTestingUtils implements TestingUtils {

    protected String suite;

    @Override
    public String getSuite() {
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
