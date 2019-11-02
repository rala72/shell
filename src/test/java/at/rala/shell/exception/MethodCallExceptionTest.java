package at.rala.shell.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MethodCallExceptionTest {
    @Test
    void testMessage() {
        Assertions.assertThrows(MethodCallException.class, () -> {
            throw new MethodCallException("message");
        }, "message");
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
