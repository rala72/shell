package io.rala.shell.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IllegalParameterExceptionTest {
    @Test
    void newOnlyOneArrayInstance() {
        try {
            throw IllegalParameterException.createNewOnlyOneDynamicInstance("method");
        } catch (IllegalParameterException e) {
            assertEquals(
                "method: may only have one dynamic parameter",
                e.getMessage()
            );
        }
    }

    @Test
    void newOnlyLastArrayOrVararg() {
        try {
            throw IllegalParameterException.createNewOnlyLastDynamicInstance("method");
        } catch (IllegalParameterException e) {
            assertEquals(
                "method: only last parameter may be dynamic",
                e.getMessage()
            );
        }
    }

    @Test
    void newIfInputNoOther() {
        try {
            throw IllegalParameterException.createNewIfInputNoOther("method");
        } catch (IllegalParameterException e) {
            assertEquals(
                "method: if input present, no other parameter allowed",
                e.getMessage()
            );
        }
    }

    @Test
    void newOnlyLastParametersCanBeAbsent() {
        try {
            throw IllegalParameterException.createNewOnlyLastParametersCanBeAbsent("method");
        } catch (IllegalParameterException e) {
            assertEquals(
                "method: only last parameters can be absent",
                e.getMessage()
            );
        }
    }
}
