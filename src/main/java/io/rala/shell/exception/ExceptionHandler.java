package io.rala.shell.exception;

import io.rala.shell.Context;

public interface ExceptionHandler {
    void handleException(Exception exception, Context context);
}
