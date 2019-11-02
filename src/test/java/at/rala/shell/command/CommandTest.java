package at.rala.shell.command;

import at.rala.shell.Context;
import at.rala.shell.Input;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandTest {
    @Test
    void testDefaultCommand() {
        EmptyCommand emptyCommand = new EmptyCommand();
        Assertions.assertNull(emptyCommand.getDocumentation());
        Assertions.assertNull(emptyCommand.getUsage());
    }

    private static class EmptyCommand implements Command {
        @Override
        public void execute(Input input, Context context) {

        }
    }
}
