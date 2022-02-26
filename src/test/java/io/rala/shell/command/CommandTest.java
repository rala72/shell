package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandTest {
    @Test
    void defaultCommand() {
        EmptyCommand emptyCommand = new EmptyCommand();
        assertThat(emptyCommand.getDocumentation()).isNull();
        assertThat(emptyCommand.getUsage()).isNull();
    }

    private static class EmptyCommand implements Command {
        @Override
        public void execute(@NotNull Input input, @NotNull Context context) {

        }
    }
}
