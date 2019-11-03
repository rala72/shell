package io.rala.shell.exception;

@SuppressWarnings("unused")
public class CommandAlreadyPresentException extends RuntimeException {
    // private static final String MESSAGE = "command already present: ";

    public CommandAlreadyPresentException(String command) {
        super(command);
    }
}