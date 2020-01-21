package io.rala.shell.exception;

import io.rala.shell.Context;

/**
 * this interface allows to customize exception handling
 */
public interface ExceptionHandler {
    /**
     * called if a exception occurs
     *
     * @param exception current thrown exception
     * @param context   context of thrown exception
     */
    void handleException(Exception exception, Context context);
}
