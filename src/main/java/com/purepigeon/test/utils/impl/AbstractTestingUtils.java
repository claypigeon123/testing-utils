package com.purepigeon.test.utils.impl;

import com.purepigeon.test.utils.TestingUtils;
import org.springframework.lang.Nullable;

/**
 * <p>
 *     Abstract class that encapsulates some functionalities that are likely common, regardless of binding.
 * </p>
 * <p>
 *     Classes that want to implement the {@link TestingUtils} interface should extend this class instead.
 * </p>
 */
public abstract class AbstractTestingUtils implements TestingUtils {

    @Nullable
    protected String suite;

    @Override
    public String getSuite() {
        if (suite == null || suite.isBlank()) {
            throw new IllegalStateException("Suite property is not set");
        }
        return suite;
    }

    @Override
    public void setSuite(@Nullable String suite) {
        this.suite = suite;
    }
}
