package com.purepigeon.test.utils.util;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeRef<T> {

    @Getter
    protected final Type type;

    protected TypeRef() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("TypeRef has been constructed without actual type info");
        }

        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public String getSimpleName() {
        String[] tokens = type.getTypeName()
            .replaceAll("<.*>", "")
            .split("\\.");

        return tokens[tokens.length - 1];
    }
}
