package io.rala.shell.exception;

/**
 * indicates that a command is defined multiple times
 *
 * @since 1.0.0
 */
public class CommandAlreadyPresentException extends RuntimeException {
    // private static final String MESSAGE = "command already present: ";

    /**
     * @param command command which is defined multiple times
     * @since 1.0.0
     */
    public CommandAlreadyPresentException(String command) {
        super(command);
    }
}
