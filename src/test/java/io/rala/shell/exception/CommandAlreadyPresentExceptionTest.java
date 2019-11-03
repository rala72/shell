package io.rala.shell.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandAlreadyPresentExceptionTest {
    @Test
    void testMessage() {
        try {
            throw new CommandAlreadyPresentException("cmd");
        } catch (CommandAlreadyPresentException e) {
            Assertions.assertEquals("cmd", e.getMessage());
        }
    }
}
