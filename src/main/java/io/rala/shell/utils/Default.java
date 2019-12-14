package io.rala.shell.utils;

import java.lang.reflect.Array;

public class Default {
    private Default() {
    }

    public static <T> T of(Class<T> c) {
        //noinspection unchecked
        return (T) Array.get(Array.newInstance(c, 1), 0);
    }
}
