package at.rala.shell.command;

import at.rala.shell.Input;
import at.rala.shell.utils.TestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class HelpCommandTest {
    private static final String COMMAND = "help";
    private static final String DOCUMENTATION = "prints help of all commands or provided ones";
    private static final String USAGE = "help [command [command ...]]";

    @Test
    void testDocumentation() {
        Assertions.assertEquals(DOCUMENTATION, new HelpCommand().getDocumentation());
    }

    @Test
    void testUsage() {
        Assertions.assertEquals(USAGE, new HelpCommand().getUsage());
    }

    @Test
    void testExecuteWithoutArguments() {
        TestContext testContext = new TestContext();

        new HelpCommand().execute(new Input(COMMAND), testContext);

        List<String> outputs = testContext.getCacheOutputStream().getOutputs();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("no commands found"));
    }

    @Test
    void testExecuteWithHelp() {
        HelpCommand helpCommand = new HelpCommand();
        TestContext testContext = new TestContext(Map.of("help", helpCommand));

        helpCommand.execute(new Input(COMMAND), testContext);

        List<String> outputs = testContext.getCacheOutputStream().getOutputs();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("help \tprints help of all commands or provided ones"));
    }

    @Test
    void testExecuteWithExitAndHelp() {
        HelpCommand helpCommand = new HelpCommand();
        Map<String, Command> commandMap = Map.of("help", helpCommand, "exit", new ExitCommand());
        TestContext testContext = new TestContext(commandMap);

        helpCommand.execute(new Input(COMMAND), testContext);

        List<String> outputs = testContext.getCacheOutputStream().getOutputs();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(2, outputs.size());
        Assertions.assertTrue(outputs.contains("help \tprints help of all commands or provided ones"));
        Assertions.assertTrue(outputs.contains("exit \tstops shell"));
    }

    @Test
    void testExecuteWithExitAndHelpAndExitAsArgument() {
        HelpCommand helpCommand = new HelpCommand();
        Map<String, Command> commandMap = Map.of("help", helpCommand, "exit", new ExitCommand());
        TestContext testContext = new TestContext(commandMap);

        helpCommand.execute(new Input(COMMAND, "exit"), testContext);

        List<String> outputs = testContext.getCacheOutputStream().getOutputs();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertFalse(outputs.contains("help \tprints help of all commands or provided ones"));
        Assertions.assertTrue(outputs.contains("exit \tstops shell"));
    }
}
