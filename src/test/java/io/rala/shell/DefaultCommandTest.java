package io.rala.shell;

import io.rala.shell.command.ExitCommand;
import io.rala.shell.command.HelpCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultCommandTest {
    @Test
    void exit() {
        DefaultCommand exit = DefaultCommand.EXIT;
        assertEquals("exit", exit.getName());
        assertEquals(exit.getName(), exit.toString());
        assertTrue(exit.getCommand() instanceof ExitCommand);
    }

    @Test
    void help() {
        DefaultCommand help = DefaultCommand.HELP;
        assertEquals("help", help.getName());
        assertEquals(help.getName(), help.toString());
        assertTrue(help.getCommand() instanceof HelpCommand);
    }
}
