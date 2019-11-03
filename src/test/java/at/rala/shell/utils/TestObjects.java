package at.rala.shell.utils;

import at.rala.shell.annotation.Command;
import at.rala.shell.annotation.CommandLoader;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TestObjects {
    private TestObjects() {
    }

    public static CommandLoader getCommandLoaderFromTestObjectWithoutAttributes() {
        return new CommandLoader(new TestObjectWithoutAttributes());
    }

    public static CommandLoader getCommandLoaderFromTestObjectWithAttributes() {
        return new CommandLoader(new TestObjectWithAttributes());
    }

    public static CommandLoader getCommandLoaderFromCommandNotUniqueErrorTestObject() {
        return new CommandLoader(new CommandNotUniqueErrorTestObject());
    }

    public static CommandLoader getCommandLoaderFromIllegalAccessErrorTestObject() {
        return new CommandLoader(new IllegalAccessErrorTestObject());
    }

    public static class TestObjectWithoutAttributes {
        @Command
        public void commandWithoutAttributes() {
        }
    }

    public static class TestObjectWithAttributes {
        @Command(value = "cmd", documentation = "documentation", usage = "usage")
        public void commandWithAttributes() {
        }
    }

    public static class CommandNotUniqueErrorTestObject {
        @Command("cmd")
        public void commandWithoutAttributes1() {
        }

        @Command("cmd")
        public void commandWithoutAttributes2() {
        }
    }

    public static class IllegalAccessErrorTestObject {
        @Command
        private void privateMethod() {
        }
    }
}
