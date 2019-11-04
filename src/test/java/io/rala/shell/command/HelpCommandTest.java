package io.rala.shell.command;

import io.rala.shell.Input;
import io.rala.shell.annotation.CommandLoader;
import io.rala.shell.utils.TestContext;
import io.rala.shell.utils.TestObjects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

class HelpCommandTest {
    private static final String COMMAND = "help";
    private static final String DOCUMENTATION = "prints help of all commands or provided ones";
    private static final String USAGE = "help [command [command ...]]";
    private static final Command EMPTY_COMMAND = (input, context) -> {
    };

    @Test
    void documentation() {
        Assertions.assertEquals(DOCUMENTATION, new HelpCommand().getDocumentation());
    }

    @Test
    void usage() {
        Assertions.assertEquals(USAGE, new HelpCommand().getUsage());
    }

    @Test
    void executeWithoutArguments() {
        TestContext testContext = new TestContext();

        new HelpCommand().execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("no commands found"));
    }

    @Test
    void executeWithInvalidArguments() {
        TestContext testContext = new TestContext();

        new HelpCommand().execute(new Input(COMMAND, "cmd"), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("error: command cmd not found"));
    }

    @Test
    void executeWithCommandWithoutDocumentation() {
        HelpCommand helpCommand = new HelpCommand();
        TestContext testContext = new TestContext(Map.of("cmd", EMPTY_COMMAND));

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("cmd \t<no documentation found>"));
    }

    @Test
    void executeWithHelp() {
        HelpCommand helpCommand = new HelpCommand();
        TestContext testContext = new TestContext(Map.of("help", helpCommand));

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("help \tprints help of all commands or provided ones"));
    }

    @Test
    void executeWithExitAndHelp() {
        HelpCommand helpCommand = new HelpCommand();
        Map<String, Command> commandMap = Map.of("help", helpCommand, "exit", new ExitCommand());
        TestContext testContext = new TestContext(commandMap);

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(2, outputs.size());
        Assertions.assertTrue(outputs.contains("help \tprints help of all commands or provided ones"));
        Assertions.assertTrue(outputs.contains("exit \tstops shell"));
    }

    @Test
    void executeWithExitAndHelpAndExitAsArgument() {
        HelpCommand helpCommand = new HelpCommand();
        Map<String, Command> commandMap = Map.of("help", helpCommand, "exit", new ExitCommand());
        TestContext testContext = new TestContext(commandMap);

        helpCommand.execute(new Input(COMMAND, "exit"), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertFalse(outputs.contains("help \tprints help of all commands or provided ones"));
        Assertions.assertTrue(outputs.contains("exit \tstops shell"));
    }

    @Test
    void executeWithTestObjectWithoutArguments() {
        CommandLoader commandLoader = TestObjects.getCommandLoaderForTestObjectWithoutAttributes();
        TestContext testContext = new TestContext(commandLoader.getCommandMethodMap());
        HelpCommand helpCommand = new HelpCommand();

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("commandWithoutAttributes \t<no documentation found>"));
    }

    @Test
    void executeWithTestObjectWithArguments() {
        CommandLoader commandLoader = TestObjects.getCommandLoaderForTestObjectWithAttributes();
        TestContext testContext = new TestContext(commandLoader.getCommandMethodMap());
        HelpCommand helpCommand = new HelpCommand();

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("cmd \tdocumentation"));
    }

    @Test
    void toStringOfCommand() {
        Assertions.assertEquals("HelpCommand", new HelpCommand().toString());
    }
}
