package io.rala.shell.exception;

/**
 * indicates that a command is defined multiple times
 */
public class CommandAlreadyPresentException extends RuntimeException {
    // private static final String MESSAGE = "command already present: ";

    /**
     * @param command command which is defined multiple times
     */
    public CommandAlreadyPresentException(String command) {
        super(command);
    }
}
