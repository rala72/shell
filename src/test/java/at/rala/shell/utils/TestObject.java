package at.rala.shell.utils;

import at.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class TestObject {
    @Command
    public void simpleCommandWithoutParameter() {
    }

    @Command
    public void simpleCommandWithOneStringParameter(String s) {
    }

    @Command
    public void simpleCommandWithTwoStringParameter(String s1, String s2) {
    }

    @Command
    public void simpleCommandWithOneStringVarargsParameter(String... s) {
    }

    @Command
    public void simpleCommandWithOneStringArrayParameter(String[] s) {
    }
}
