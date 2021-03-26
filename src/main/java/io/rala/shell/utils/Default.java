package io.rala.shell.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;

/**
 * class which allows getting the default value of requested classes
 *
 * @since 1.0.0
 */
public class Default {
    private Default() {
    }

    /**
     * @param c   class to get default value from
     * @param <T> type of class to get default value from
     * @return default value of specified class - may be {@code null}
     * @since 1.0.0
     */
    @Nullable
    public static <T> T of(@NotNull Class<T> c) {
        //noinspection unchecked
        return (T) Array.get(Array.newInstance(c, 1), 0);
    }
}
