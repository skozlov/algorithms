package com.github.skozlov.algorithms.util;

import java.util.List;

public class ListUtils {
    public static <T> void swap(List<T> list, int i, int j) {
        var buf = list.get(i);
        list.set(i, list.get(j));
        list.set(j, buf);
    }
}
