package io.rala.shell.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StopShellExceptionTest {
    @Test
    void test() {
        assertThrows(StopShellException.class, () -> {
            throw new StopShellException();
        });
    }
}
