package io.rala.shell.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class StopShellExceptionTest {
    @Test
    void test() {
        assertThatExceptionOfType(StopShellException.class).isThrownBy(() -> {
            throw new StopShellException();
        });
    }
}
