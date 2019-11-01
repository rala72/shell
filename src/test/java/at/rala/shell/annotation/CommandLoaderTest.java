package at.rala.shell.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandLoaderTest {
    @Test
    void testSimpleCommandMethodLoading() {
        CommandLoader commandLoader = new CommandLoader(new TestObject());
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("simpleCommand"));
    }

    @SuppressWarnings({"unused"})
    private static class TestObject {
        @Command
        public void simpleCommand() {
        }
    }
}
