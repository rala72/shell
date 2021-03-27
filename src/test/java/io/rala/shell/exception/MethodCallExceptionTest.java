package io.rala.shell.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MethodCallExceptionTest {
    @Test
    void message() {
        try {
            throw new MethodCallException("message");
        } catch (MethodCallException e) {
            assertEquals("message", e.getMessage());
        }
    }

    @Test
    void throwable() {
        try {
            throw new MethodCallException(new IllegalArgumentException());
        } catch (MethodCallException e) {
            assertEquals(e.getCause().getClass(), IllegalArgumentException.class);
        }
    }
}
