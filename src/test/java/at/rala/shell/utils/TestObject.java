package at.rala.shell.utils;

import at.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class TestObject {
    @Command
    public void commandWithoutAttributesAndMethodWithoutParameter() {
    }

    @Command
    public void commandWithoutAttributesAndMethodWithOneStringParameter(String s) {
    }

    @Command
    public void commandWithoutAttributesAndMethodWithTwoStringParameter(String s1, String s2) {
    }

    @Command
    public void commandWithoutAttributesAndMethodWithOneStringVarargsParameter(String... s) {
    }

    @Command
    public void commandWithoutAttributesAndMethodWithOneStringArrayParameter(String[] s) {
    }

    @Override
    public String toString() {
        return "TestObject";
    }
}
