package at.rala.shell.command;

import at.rala.shell.Input;
import at.rala.shell.exception.StopShellException;
import at.rala.shell.utils.TestContext;
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
    void testExecute() {
        try {
            new ExitCommand().execute(new Input(COMMAND), new TestContext());
        } catch (StopShellException e) {
            return;
        }
        Assertions.fail();
    }
}
