package at.rala.shell.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandLoaderTest {
    @Test
    void testCommandWithoutAttributesLoading() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithoutAttributes());
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("commandWithoutAttributes"));
    }

    @Test
    void testCommandWithAttributesLoading() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithAttributes());
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("cmd"));
    }

    @Test
    void testCommandNotUniqueError() {
        Assertions.assertThrows(IllegalStateException.class,
            () -> new CommandLoader(new CommandNotUniqueErrorTestObject())
        );
    }

    @Test
    void testCommandIllegalAccessError() {
        CommandLoader commandLoader = new CommandLoader(new IllegalAccessErrorTestObject());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().isEmpty());
    }

    @Test
    void testToString() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithoutAttributes());
        Assertions.assertEquals("commandWithoutAttributes", commandLoader.toString());
    }

    // region TestObject

    @SuppressWarnings({"unused"})
    private static class TestObjectWithoutAttributes {
        @Command
        public void commandWithoutAttributes() {
        }
    }

    @SuppressWarnings({"unused"})
    private static class TestObjectWithAttributes {
        @Command("cmd")
        public void commandWithAttributes() {
        }
    }

    @SuppressWarnings({"unused"})
    private static class CommandNotUniqueErrorTestObject {
        @Command("cmd")
        public void commandWithoutAttributes1() {
        }

        @Command("cmd")
        public void commandWithoutAttributes2() {
        }
    }

    @SuppressWarnings({"unused"})
    private static class IllegalAccessErrorTestObject {
        @Command
        private void privateMethod() {
        }
    }

    // endregion
}
