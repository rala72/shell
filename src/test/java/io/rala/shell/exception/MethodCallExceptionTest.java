package io.rala.shell.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodCallExceptionTest {
    @Test
    void message() {
        try {
            throw new MethodCallException("message");
        } catch (MethodCallException e) {
            assertThat(e.getMessage()).isEqualTo("message");
        }
    }

    @Test
    void throwable() {
        try {
            throw new MethodCallException(new IllegalArgumentException());
        } catch (MethodCallException e) {
            assertThat(IllegalArgumentException.class).isEqualTo(e.getCause().getClass());
        }
    }
}
