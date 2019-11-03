package at.rala.shell.annotation;

import at.rala.shell.utils.TestObjects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandLoaderTest {
    @Test
    void testCommandWithoutAttributesLoading() {
        CommandLoader commandLoader = TestObjects.getCommandLoaderFromTestObjectWithoutAttributes();
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("commandWithoutAttributes"));
    }

    @Test
    void testCommandWithAttributesLoading() {
        CommandLoader commandLoader = TestObjects.getCommandLoaderFromTestObjectWithAttributes();
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("cmd"));
    }

    @Test
    void testCommandNotUniqueError() {
        Assertions.assertThrows(IllegalStateException.class,
            () -> new CommandLoader(new TestObjects.CommandNotUniqueErrorTestObject())
        );
    }

    @Test
    void testCommandIllegalAccessError() {
        CommandLoader commandLoader = TestObjects.getCommandLoaderFromIllegalAccessErrorTestObject();
        Assertions.assertTrue(commandLoader.getCommandMethodMap().isEmpty());
    }

    @Test
    void testToString() {
        CommandLoader commandLoader = new CommandLoader(new TestObjects.TestObjectWithoutAttributes());
        Assertions.assertEquals("commandWithoutAttributes", commandLoader.toString());
    }
}
