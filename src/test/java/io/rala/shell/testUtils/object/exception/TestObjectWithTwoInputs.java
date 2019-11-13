package io.rala.shell.testUtils.object.exception;

import io.rala.shell.Input;
import io.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class TestObjectWithTwoInputs {
    @Command
    public void commandWithTwoInputs(Input input1, Input input2) {
    }
}
