package io.rala.shell.command;

import io.rala.shell.Input;
import io.rala.shell.exception.StopShellException;
import io.rala.shell.testUtils.TestContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExitCommandTest {
    private static final String COMMAND = "exit";
    private static final String DOCUMENTATION = "stops shell";
    private static final String USAGE = "exit";

    @Test
    void documentation() {
        assertEquals(DOCUMENTATION, new ExitCommand().getDocumentation());
    }

    @Test
    void usage() {
        assertEquals(USAGE, new ExitCommand().getUsage());
    }

    @Test
    void executeWithoutParameters() {
        assertThrows(StopShellException.class, () ->
            new ExitCommand().execute(new Input(COMMAND), new TestContext())
        );
    }

    @Test
    void executeWithParameters() {
        TestContext testContext = TestContext.getInstanceWithDifferentStreams();
        new ExitCommand().execute(new Input(COMMAND, "cmd"), testContext);
        assertTrue(testContext.getOutputHistory().isEmpty());
        assertFalse(testContext.getErrorHistory().isEmpty());
        assertTrue(testContext.getErrorHistory().contains("error: no arguments expected"));
    }

    @Test
    void toStringOfCommand() {
        assertEquals("ExitCommand", new ExitCommand().toString());
    }
}
