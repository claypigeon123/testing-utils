package com.purepigeon.test.utils.impl.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.purepigeon.test.utils.util.TypeRef;

import java.lang.reflect.Type;

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
