package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class CommandTest {
    @Test
    void defaultCommand() {
        EmptyCommand emptyCommand = new EmptyCommand();
        assertNull(emptyCommand.getDocumentation());
        assertNull(emptyCommand.getUsage());
    }

    private static class EmptyCommand implements Command {
        @Override
        public void execute(@NotNull Input input, @NotNull Context context) {

        }
    }
}
