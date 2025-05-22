package com.purepigeon.test.utils;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 *     Testing utils' custom type reference to resolve types with generics.
 * </p>
 * <p>
 *     Sample usage:
 * </p>
 * <pre>
 * {@code
 *     // ...
 *     @SpringBootTest
 *     @WithTestingUtils
 *     class SampleTest {
 *
 *         @Autowired
 *         private TestingUtils testingUtils;
 *
 *         @Test
 *         void sampleTest(String testCase) {
 *             var inputList = testingUtils.readInputObject(testCase, new TypeRef<List<SampleData>>() {});
 *
 *             // ...
 *         }
 *     }
 * }
 * </pre>
 * @param <T> The referenced type
 */
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
