package io.rala.shell.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MethodCallExceptionTest {
    @Test
    void testMessage() {
        try {
            throw new MethodCallException("message");
        } catch (MethodCallException e) {
            Assertions.assertEquals("message", e.getMessage());
        }
    }

    @Test
    void testThrowable() {
        try {
            throw new MethodCallException(new IllegalArgumentException());
        } catch (MethodCallException e) {
            Assertions.assertEquals(e.getCause().getClass(), IllegalArgumentException.class);
        }
    }
}
