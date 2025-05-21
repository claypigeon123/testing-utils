package com.purepigeon.test.utils.extension;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DirectExtensionTest {

    @Test
    void noWithTestingUtilsAnnotation() {
        // given
        TestingUtilsExtension extension = new TestingUtilsExtension();

        // expect
        var ex = assertThrows(IllegalStateException.class, () -> extension.postProcessTestInstance(this, null));
        assertEquals("No @WithTestingUtils annotation found", ex.getMessage());
    }
}
