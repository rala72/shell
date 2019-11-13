package io.rala.shell.testUtils.object.exception;

import io.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class TestObjectWithTwoArrays {
    @Command
    public void commandWithTwoArrays(String[] array1, String... varargs) {
    }
}
