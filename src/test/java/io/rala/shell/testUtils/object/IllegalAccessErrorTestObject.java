package io.rala.shell.testUtils.object;

import io.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class IllegalAccessErrorTestObject {
    @Command
    private void privateMethod() {
    }
}
