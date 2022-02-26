package io.rala.shell.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandAlreadyPresentExceptionTest {
    @Test
    void message() {
        try {
            throw new CommandAlreadyPresentException("cmd");
        } catch (CommandAlreadyPresentException e) {
            assertThat(e.getMessage()).isEqualTo("cmd");
        }
    }
}
