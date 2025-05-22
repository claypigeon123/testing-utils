package com.purepigeon.test.utils.impl.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.purepigeon.test.utils.TypeRef;

import java.lang.reflect.Type;

/**
 * <p>
 *     Helper class to connect Testing utils' {@link TypeRef} to Jackson's {@link TypeReference}.
 * </p>
 * @param <T> The referenced type
 */
public class JacksonTypeRefConnector<T> extends TypeReference<T> {

    private final Type type;

    public JacksonTypeRefConnector(TypeRef<T> ref) {
        this.type = ref.getType();
    }

    @Override
    public Type getType() {
        return type;
    }
}
