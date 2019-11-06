package io.rala.shell.utils.object;

import io.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class TestObjectWithAttributes {
    @Command(value = "cmd", documentation = "documentation", usage = "usage")
    public void commandWithAttributes() {
    }
}
