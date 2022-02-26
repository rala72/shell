package io.rala.shell;

import io.rala.shell.command.ExitCommand;
import io.rala.shell.command.HelpCommand;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultCommandTest {
    @Test
    void exit() {
        DefaultCommand exit = DefaultCommand.EXIT;
        assertThat(exit.getName()).isEqualTo("exit");
        assertThat(exit).hasToString(exit.getName());
        assertThat(exit.getCommand()).isInstanceOf(ExitCommand.class);
    }

    @Test
    void help() {
        DefaultCommand help = DefaultCommand.HELP;
        assertThat(help.getName()).isEqualTo("help");
        assertThat(help).hasToString(help.getName());
        assertThat(help.getCommand()).isInstanceOf(HelpCommand.class);
    }
}
