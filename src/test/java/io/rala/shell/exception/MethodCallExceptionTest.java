package io.rala.shell.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MethodCallExceptionTest {
    @Test
    void message() {
        try {
            throw new MethodCallException("message");
        } catch (MethodCallException e) {
            Assertions.assertEquals("message", e.getMessage());
        }
    }

    @Test
    void throwable() {
        try {
            throw new MethodCallException(new IllegalArgumentException());
        } catch (MethodCallException e) {
            Assertions.assertEquals(e.getCause().getClass(), IllegalArgumentException.class);
        }
    }
}
