package at.rala.shell;

import at.rala.shell.command.ExitCommand;
import at.rala.shell.utils.TestContext;
import at.rala.shell.utils.io.CacheOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

class ContextTest {
    @Test
    void testEmptyCommands() {
        Assertions.assertTrue(new TestContext().getCommands().isEmpty());
    }

    @Test
    void testCommandsWithExit() {
        TestContext testContext = new TestContext(Map.of("help", new ExitCommand()));
        Assertions.assertFalse(testContext.getCommands().isEmpty());
        Assertions.assertTrue(testContext.getCommands().containsKey("help"));
        Assertions.assertNotNull(testContext.getCommands().get("help"));
    }

    @Test
    void testOutputAndError() {
        TestContext testContext = new TestContext();

        testContext.printLine("line");

        BlockingQueue<String> outputs = testContext.getCacheOutputStream().getOutputs();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("line"));

        testContext.printError("error");

        BlockingQueue<String> errors = testContext.getCacheErrorStream().getOutputs();
        Assertions.assertEquals(outputs, errors);
    }

    @Test
    void testErrorButNotOutput() {
        TestContext testContext = new TestContext(new CacheOutputStream(), new CacheOutputStream());

        testContext.printError("error");

        BlockingQueue<String> errors = testContext.getCacheErrorStream().getOutputs();
        Assertions.assertFalse(errors.isEmpty());
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.contains("error"));

        BlockingQueue<String> outputs = testContext.getCacheOutputStream().getOutputs();
        Assertions.assertTrue(outputs.isEmpty());
        Assertions.assertEquals(0, outputs.size());
        Assertions.assertFalse(outputs.contains("error"));
    }

    @Test
    void testToString() {
        String toString = "Context{output==error=false, commands={}}";
        Assertions.assertEquals(toString, new TestContext().toString());
    }
}
