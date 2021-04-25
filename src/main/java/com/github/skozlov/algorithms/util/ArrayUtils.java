package com.github.skozlov.algorithms.util;

import java.lang.reflect.Array;

import static java.lang.System.arraycopy;

public class ArrayUtils {
    public static <T> T[] create(Class<T> elementType, int length) {
        @SuppressWarnings("unchecked")
        var result = (T[]) Array.newInstance(elementType, length);
        return result;
    }

    public static <T> T[] copy(T[] source, Class<T> elementType) {
        T[] target = create(elementType, source.length);
        arraycopy(source, 0, target, 0, source.length);
        return target;
    }
}
