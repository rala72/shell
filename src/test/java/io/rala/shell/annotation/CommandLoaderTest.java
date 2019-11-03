package io.rala.shell.annotation;

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
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> new CommandLoader(new TestObjects.TestObjectWithTwoArrays()),
            "commandWithTwoArrays: may only have one array parameter");
    }

    @Test
    void testCommandWithOneArrayParameterNotOnEndException() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> new CommandLoader(new TestObjects.TestObjectWithArrayNotOnEnd()),
            "commandWithArrayNotOnEnd: may only have one array parameter");
    }

    @Test
    void testCommandNotUniqueException() {
        Assertions.assertThrows(IllegalStateException.class,
            () -> new CommandLoader(new TestObjects.CommandNotUniqueErrorTestObject())
        );
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
