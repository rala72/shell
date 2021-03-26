package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandTest {
    @Test
    void defaultCommand() {
        EmptyCommand emptyCommand = new EmptyCommand();
        Assertions.assertNull(emptyCommand.getDocumentation());
        Assertions.assertNull(emptyCommand.getUsage());
    }

    private static class EmptyCommand implements Command {
        @Override
        public void execute(@NotNull Input input, @NotNull Context context) {

        }
    }
}
