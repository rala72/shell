package io.rala.shell.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandAlreadyPresentExceptionTest {
    @Test
    void message() {
        try {
            throw new CommandAlreadyPresentException("cmd");
        } catch (CommandAlreadyPresentException e) {
            assertEquals("cmd", e.getMessage());
        }
    }
}
