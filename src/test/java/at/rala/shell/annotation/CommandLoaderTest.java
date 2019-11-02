package at.rala.shell.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandLoaderTest {
    @Test
    void testSimpleCommandMethodLoading() {
        CommandLoader commandLoader = new CommandLoader(new TestObject());
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("commandWithoutAttributes"));
    }

    @Test
    void testErrorCommandMethodLoading() {
        Assertions.assertThrows(IllegalStateException.class,
            () -> new CommandLoader(new ErrorTestObject())
        );
    }

    @Test
    void testToString() {
        CommandLoader commandLoader = new CommandLoader(new TestObject());
        Assertions.assertEquals("commandWithoutAttributes", commandLoader.toString());
    }

    @SuppressWarnings({"unused"})
    private static class TestObject {
        @Command
        public void commandWithoutAttributes() {
        }
    }

    @SuppressWarnings({"unused"})
    private static class ErrorTestObject {
        @Command("cmd")
        public void commandWithoutAttributes1() {
        }

        @Command("cmd")
        public void commandWithoutAttributes2() {
        }
    }
}
