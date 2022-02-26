package io.rala.shell.command;

import io.rala.shell.Input;
import io.rala.shell.annotation.CommandLoader;
import io.rala.shell.testUtils.CommandLoaderFactory;
import io.rala.shell.testUtils.TestContext;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

class HelpCommandTest {
    private static final String COMMAND = "help";
    private static final String DOCUMENTATION = "prints help of all commands or provided ones";
    private static final String USAGE = "help [command [command ...]]";
    private static final Command EMPTY_COMMAND = (input, context) -> {
    };

    @Test
    void documentation() {
        assertThat(new HelpCommand().getDocumentation()).isEqualTo(DOCUMENTATION);
    }

    @Test
    void usage() {
        assertThat(new HelpCommand().getUsage()).isEqualTo(USAGE);
    }

    @Test
    void executeWithoutArguments() {
        TestContext testContext = new TestContext();

        new HelpCommand().execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs)
            .hasSize(1).contains("no commands found");
    }

    @Test
    void executeWithInvalidArguments() {
        TestContext testContext = new TestContext();

        new HelpCommand().execute(new Input(COMMAND, "cmd"), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs)
            .hasSize(1)
            .contains("error: command cmd not found");
    }

    @Test
    void executeWithCommandWithoutDocumentation() {
        HelpCommand helpCommand = new HelpCommand();
        TestContext testContext = new TestContext(Map.of("cmd", EMPTY_COMMAND));

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs)
            .hasSize(1)
            .contains("cmd \t<no documentation found>");
    }

    @Test
    void executeWithHelp() {
        HelpCommand helpCommand = new HelpCommand();
        TestContext testContext = new TestContext(Map.of("help", helpCommand));

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs)
            .hasSize(1)
            .contains("help \tprints help of all commands or provided ones");
    }

    @Test
    void executeWithExitAndHelp() {
        HelpCommand helpCommand = new HelpCommand();
        Map<String, Command> commandMap = Map.of("help", helpCommand, "exit", new ExitCommand());
        TestContext testContext = new TestContext(commandMap);

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs)
            .hasSize(2).contains("help \tprints help of all commands or provided ones", "exit \tstops shell");
    }

    @Test
    void executeWithExitAndHelpAndExitAsArgument() {
        HelpCommand helpCommand = new HelpCommand();
        Map<String, Command> commandMap = Map.of("help", helpCommand, "exit", new ExitCommand());
        TestContext testContext = new TestContext(commandMap);

        helpCommand.execute(new Input(COMMAND, "exit"), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs)
            .hasSize(1)
            .doesNotContain("help \tprints help of all commands or provided ones")
            .contains("exit \tstops shell");
    }

    @Test
    void executeWithTestObjectWithoutArguments() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithoutAttributes();
        TestContext testContext = new TestContext(commandLoader.getCommandMethodMap());
        HelpCommand helpCommand = new HelpCommand();

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs)
            .hasSize(1)
            .contains("commandWithoutAttributes \t<no documentation found>");
    }

    @Test
    void executeWithTestObjectWithArguments() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithAttributes();
        TestContext testContext = new TestContext(commandLoader.getCommandMethodMap());
        HelpCommand helpCommand = new HelpCommand();

        helpCommand.execute(new Input(COMMAND), testContext);

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs)
            .hasSize(1)
            .contains("cmd \tdocumentation");
    }

    @Test
    void toStringOfCommand() {
        assertThat(new HelpCommand()).hasToString("HelpCommand");
    }
}
