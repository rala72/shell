package at.rala.shell;

import at.rala.shell.command.ExitCommand;
import at.rala.shell.command.HelpCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultCommandTest {
    @Test
    void testExit() {
        DefaultCommand exit = DefaultCommand.EXIT;
        Assertions.assertEquals("exit", exit.getName());
        Assertions.assertEquals(exit.getName(), exit.toString());
        Assertions.assertTrue(exit.getCommand() instanceof ExitCommand);
    }

    @Test
    void testHelp() {
        DefaultCommand help = DefaultCommand.HELP;
        Assertions.assertEquals("help", help.getName());
        Assertions.assertEquals(help.getName(), help.toString());
        Assertions.assertTrue(help.getCommand() instanceof HelpCommand);
    }
}
