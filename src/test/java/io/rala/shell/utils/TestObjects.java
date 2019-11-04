package io.rala.shell.utils;

import io.rala.shell.annotation.Command;
import io.rala.shell.annotation.CommandLoader;

import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class TestObjects {
    private TestObjects() {
    }

    public static CommandLoader getCommandLoaderForTestObjectWithoutAttributes() {
        return new CommandLoader(new TestObjectWithoutAttributes());
    }

    public static CommandLoader getCommandLoaderForTestObjectWithAttributes() {
        return new CommandLoader(new TestObjectWithAttributes());
    }

    public static CommandLoader getCommandLoaderForIllegalAccessErrorTestObject() {
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

    public static class TestObjectWithTwoArrays {
        @Command
        public void commandWithTwoArrays(String[] array1, String... varargs) {
        }
    }

    public static class TestObjectWithTwoLists {
        @Command
        public void commandWithTwoLists(List<String> list1, List<String> list2) {
        }
    }

    public static class TestObjectWithArrayNotOnEnd {
        @Command
        public void commandWithArrayNotOnEnd(String[] array, String s) {
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
