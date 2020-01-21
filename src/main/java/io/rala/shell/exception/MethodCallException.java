package io.rala.shell.exception;

/**
 * indicates that an {@link Exception} is thrown during method call
 */
public class MethodCallException extends RuntimeException {
    /**
     * @param message message of exception
     */
    public MethodCallException(String message) {
        super(message);
    }

    /**
     * @param cause cause of exception
     */
    public MethodCallException(Throwable cause) {
        super(cause);
    }
}
