package io.rala.shell.command;

import io.rala.shell.Input;
import io.rala.shell.annotation.CommandLoader;
import io.rala.shell.testUtils.CommandLoaderFactory;
import io.rala.shell.testUtils.TestContext;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class HelpCommandTest {
    private static final String COMMAND = "help";
    private static final String DOCUMENTATION = "prints help of all commands or provided ones";
    private static final String USAGE = "help [command [command ...]]";
    private static final Command EMPTY_COMMAND = (input, context) -> {
    };

    @Test
    void documentation() {
        assertEquals(DOCUMENTATION, new HelpCommand().getDocumentation());
    }

    @Test
    void usage() {
        assertEquals(USAGE, new HelpCommand().getUsage());
    }

    @Test
    void executeWithoutArguments() {
        TestContext testContext = new TestContext();

        new HelpCommand().execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertFalse(outputs.isEmpty());
        assertEquals(1, outputs.size());
        assertTrue(outputs.contains("no commands found"));
    }

    @Test
    void executeWithInvalidArguments() {
        TestContext testContext = new TestContext();

        new HelpCommand().execute(new Input(COMMAND, "cmd"), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertFalse(outputs.isEmpty());
        assertEquals(1, outputs.size());
        assertTrue(outputs.contains("error: command cmd not found"));
    }

    @Test
    void executeWithCommandWithoutDocumentation() {
        HelpCommand helpCommand = new HelpCommand();
        TestContext testContext = new TestContext(Map.of("cmd", EMPTY_COMMAND));

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertFalse(outputs.isEmpty());
        assertEquals(1, outputs.size());
        assertTrue(outputs.contains("cmd \t<no documentation found>"));
    }

    @Test
    void executeWithHelp() {
        HelpCommand helpCommand = new HelpCommand();
        TestContext testContext = new TestContext(Map.of("help", helpCommand));

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertFalse(outputs.isEmpty());
        assertEquals(1, outputs.size());
        assertTrue(outputs.contains("help \tprints help of all commands or provided ones"));
    }

    @Test
    void executeWithExitAndHelp() {
        HelpCommand helpCommand = new HelpCommand();
        Map<String, Command> commandMap = Map.of("help", helpCommand, "exit", new ExitCommand());
        TestContext testContext = new TestContext(commandMap);

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertFalse(outputs.isEmpty());
        assertEquals(2, outputs.size());
        assertTrue(outputs.contains("help \tprints help of all commands or provided ones"));
        assertTrue(outputs.contains("exit \tstops shell"));
    }

    @Test
    void executeWithExitAndHelpAndExitAsArgument() {
        HelpCommand helpCommand = new HelpCommand();
        Map<String, Command> commandMap = Map.of("help", helpCommand, "exit", new ExitCommand());
        TestContext testContext = new TestContext(commandMap);

        helpCommand.execute(new Input(COMMAND, "exit"), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertFalse(outputs.isEmpty());
        assertEquals(1, outputs.size());
        assertFalse(outputs.contains("help \tprints help of all commands or provided ones"));
        assertTrue(outputs.contains("exit \tstops shell"));
    }

    @Test
    void executeWithTestObjectWithoutArguments() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithoutAttributes();
        TestContext testContext = new TestContext(commandLoader.getCommandMethodMap());
        HelpCommand helpCommand = new HelpCommand();

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertFalse(outputs.isEmpty());
        assertEquals(1, outputs.size());
        assertTrue(outputs.contains("commandWithoutAttributes \t<no documentation found>"));
    }

    @Test
    void executeWithTestObjectWithArguments() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithAttributes();
        TestContext testContext = new TestContext(commandLoader.getCommandMethodMap());
        HelpCommand helpCommand = new HelpCommand();

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertFalse(outputs.isEmpty());
        assertEquals(1, outputs.size());
        assertTrue(outputs.contains("cmd \tdocumentation"));
    }

    @Test
    void toStringOfCommand() {
        assertEquals("HelpCommand", new HelpCommand().toString());
    }
}
