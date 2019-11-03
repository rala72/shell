package io.rala.shell.annotation;

import io.rala.shell.exception.CommandAlreadyPresentException;
import io.rala.shell.utils.TestObjects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandLoaderTest {
    @Test
    void testCommandWithoutAttributesLoading() {
        CommandLoader commandLoader = TestObjects.getCommandLoaderForTestObjectWithoutAttributes();
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("commandWithoutAttributes"));
    }

    @Test
    void testCommandWithAttributesLoading() {
        CommandLoader commandLoader = TestObjects.getCommandLoaderForTestObjectWithAttributes();
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("cmd"));
    }

    @Test
    void testCommandWithTwoArrayParameterException() {
        try {
            new CommandLoader(new TestObjects.TestObjectWithTwoArrays());
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals(
                "commandWithTwoArrays: may only have one array parameter",
                e.getMessage()
            );
            return;
        }
        Assertions.fail();
    }

    @Test
    void testCommandWithOneArrayParameterNotOnEndException() {
        try {
            new CommandLoader(new TestObjects.TestObjectWithArrayNotOnEnd());
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals(
                "commandWithArrayNotOnEnd: only last parameter may be an array or vararg",
                e.getMessage()
            );
            return;
        }
        Assertions.fail();
    }

    @Test
    void testCommandNotUniqueException() {
        try {
            new CommandLoader(new TestObjects.CommandNotUniqueErrorTestObject());
        } catch (CommandAlreadyPresentException e) {
            Assertions.assertEquals("cmd", e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void testCommandIllegalAccessException() {
        CommandLoader commandLoader = TestObjects.getCommandLoaderForIllegalAccessErrorTestObject();
        Assertions.assertTrue(commandLoader.getCommandMethodMap().isEmpty());
    }

    @Test
    void testToString() {
        CommandLoader commandLoader = new CommandLoader(new TestObjects.TestObjectWithoutAttributes());
        Assertions.assertEquals("commandWithoutAttributes", commandLoader.toString());
    }
}
