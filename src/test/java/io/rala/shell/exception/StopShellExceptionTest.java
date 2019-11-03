package io.rala.shell.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StopShellExceptionTest {
    @Test
    void test() {
        Assertions.assertThrows(StopShellException.class, () -> {
            throw new StopShellException();
        });
    }
}
