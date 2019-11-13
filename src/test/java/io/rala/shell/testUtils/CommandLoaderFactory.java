package io.rala.shell.testUtils;

import io.rala.shell.annotation.CommandLoader;
import io.rala.shell.testUtils.object.TestObjectWithAttributes;
import io.rala.shell.testUtils.object.TestObjectWithOneInput;
import io.rala.shell.testUtils.object.TestObjectWithoutAttributes;
import io.rala.shell.testUtils.object.exception.IllegalAccessErrorTestObject;

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
