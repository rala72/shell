package io.rala.shell.testUtils;

import java.util.concurrent.Callable;

public class WaitUtils {
    private WaitUtils() {
    }

    public static void waitUntilNot(Callable<Boolean> callable) {
        waitUntil(() -> !callable.call());
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
