package io.rala.shell.command;

import io.rala.shell.Input;
import io.rala.shell.exception.StopShellException;
import io.rala.shell.utils.TestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExitCommandTest {
    private static final String COMMAND = "exit";
    private static final String DOCUMENTATION = "stops shell";
    private static final String USAGE = "exit";

    @Test
    void testDocumentation() {
        Assertions.assertEquals(DOCUMENTATION, new ExitCommand().getDocumentation());
    }

    @Test
    void testUsage() {
        Assertions.assertEquals(USAGE, new ExitCommand().getUsage());
    }

    @Test
    void testExecuteWithoutParameters() {
        Assertions.assertThrows(StopShellException.class, () ->
            new ExitCommand().execute(new Input(COMMAND), new TestContext())
        );
    }

    @Test
    void testExecuteWithParameters() {
        TestContext testContext = TestContext.getInstanceWithDifferentStreams();
        new ExitCommand().execute(new Input(COMMAND, "cmd"), testContext);
        Assertions.assertTrue(testContext.getOutputHistory().isEmpty());
        Assertions.assertFalse(testContext.getErrorHistory().isEmpty());
        Assertions.assertTrue(testContext.getErrorHistory().contains("error: no arguments expected"));
    }

    @Test
    void testToString() {
        Assertions.assertEquals("ExitCommand", new ExitCommand().toString());
    }
}
