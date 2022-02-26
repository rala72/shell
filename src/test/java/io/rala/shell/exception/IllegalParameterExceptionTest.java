package io.rala.shell.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IllegalParameterExceptionTest {
    @Test
    void newOnlyOneArrayInstance() {
        try {
            throw IllegalParameterException.createNewOnlyOneDynamicInstance("method");
        } catch (IllegalParameterException e) {
            assertThat(e.getMessage()).isEqualTo("method: may only have one dynamic parameter");
        }
    }

    @Test
    void newOnlyLastArrayOrVararg() {
        try {
            throw IllegalParameterException.createNewOnlyLastDynamicInstance("method");
        } catch (IllegalParameterException e) {
            assertThat(e.getMessage()).isEqualTo("method: only last parameter may be dynamic");
        }
    }

    @Test
    void newIfInputNoOther() {
        try {
            throw IllegalParameterException.createNewIfInputNoOther("method");
        } catch (IllegalParameterException e) {
            assertThat(e.getMessage()).isEqualTo("method: if input present, no other parameter allowed");
        }
    }

    @Test
    void newOnlyLastParametersCanBeAbsent() {
        try {
            throw IllegalParameterException.createNewOnlyLastParametersCanBeAbsent("method");
        } catch (IllegalParameterException e) {
            assertThat(e.getMessage()).isEqualTo("method: only last parameters can be absent");
        }
    }
}
