package io.rala.shell.exception;

public class MethodCallException extends RuntimeException {
    public MethodCallException(String message) {
        super(message);
    }

    public MethodCallException(Throwable cause) {
        super(cause);
    }
}
