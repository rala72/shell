package io.rala.shell.exception;

import io.rala.shell.Context;

/**
 * this interface allows to customize exception handling
 *
 * @since 1.0.0
 */
public interface ExceptionHandler {
    /**
     * called if a exception occurs
     *
     * @param exception current thrown exception
     * @param context   context of thrown exception
     * @since 1.0.0
     */
    void handleException(Exception exception, Context context);
}
