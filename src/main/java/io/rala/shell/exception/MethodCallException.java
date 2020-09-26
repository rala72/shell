package io.rala.shell.exception;

/**
 * indicates that an {@link Exception} is thrown during method call
 *
 * @since 1.0.0
 */
public class MethodCallException extends RuntimeException {
    /**
     * @param message message of exception
     * @since 1.0.0
     */
    public MethodCallException(String message) {
        super(message);
    }

    /**
     * @param cause cause of exception
     * @since 1.0.0
     */
    public MethodCallException(Throwable cause) {
        super(cause);
    }
}
