package io.rala.shell.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IllegalParameterExceptionTest {
    @Test
    void newOnlyOneArrayInstance() {
        try {
            throw IllegalParameterException.createNewOnlyOneArrayInstance("method");
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "method: may only have one array parameter",
                e.getMessage()
            );
        }
    }

    @Test
    void newOnlyLastArrayOrVararg() {
        try {
            throw IllegalParameterException.createNewOnlyLastArrayOrVararg("method");
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "method: only last parameter may be an array or vararg",
                e.getMessage()
            );
        }
    }
}
