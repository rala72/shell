package io.rala.shell.utils;

import io.rala.shell.annotation.CommandLoader;
import io.rala.shell.utils.object.IllegalAccessErrorTestObject;
import io.rala.shell.utils.object.TestObjectWithAttributes;
import io.rala.shell.utils.object.TestObjectWithOneInput;
import io.rala.shell.utils.object.TestObjectWithoutAttributes;

@SuppressWarnings("unused")
public class CommandLoaderFactory {
    private CommandLoaderFactory() {
    }

    public static CommandLoader getCommandLoaderForTestObjectWithoutAttributes() {
        return new CommandLoader(new TestObjectWithoutAttributes());
    }

    public static CommandLoader getCommandLoaderForTestObjectWithAttributes() {
        return new CommandLoader(new TestObjectWithAttributes());
    }

    public static CommandLoader getCommandLoaderForTestObjectWithOneInput() {
        return new CommandLoader(new TestObjectWithOneInput());
    }

    public static CommandLoader getCommandLoaderForIllegalAccessErrorTestObject() {
        return new CommandLoader(new IllegalAccessErrorTestObject());
    }
}
