package io.rala.shell.testUtils.object.exception;

import io.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class CommandNotUniqueErrorTestObject {
    @Command("cmd")
    public void commandWithoutAttributes1() {
    }

    @Command("cmd")
    public void commandWithoutAttributes2() {
    }
}
