package com.purepigeon.test.utils;

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
