package io.rala.shell.utils;

import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public class WaitUtils {
    private WaitUtils() {
    }

    public static void waitUntil(Callable<Boolean> callable) {
        try {
            //noinspection StatementWithEmptyBody
            while (!callable.call()) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
