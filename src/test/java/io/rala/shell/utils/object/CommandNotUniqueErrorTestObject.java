package io.rala.shell.utils.object;

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
