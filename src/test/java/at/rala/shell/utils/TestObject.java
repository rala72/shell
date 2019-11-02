package at.rala.shell.utils;

import at.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class TestObject {
    @Command
    public void methodWithoutParameter() {
    }

    @Command
    public void methodWithOneStringParameter(String s) {
    }

    @Command
    public void methodWithTwoStringParameter(String s1, String s2) {
    }

    @Command
    public void methodWithOneStringVarargsParameter(String... s) {
    }

    @Command
    public void methodWithOneStringArrayParameter(String[] s) {
    }

    @Command
    public void exceptionCommand() {
        throw new RuntimeException();
    }

    @Command
    private void illegalAccessCommand() {
    }

    @Command(value = "value", documentation = "documentation", usage = "usage")
    public void commandWithAttributes() {
    }

    @Override
    public String toString() {
        return "TestObject";
    }
}
