package io.rala.shell.utils.object;

import io.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class IllegalAccessErrorTestObject {
    @Command
    private void privateMethod() {
    }
}
