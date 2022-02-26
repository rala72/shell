package io.rala.shell.command;

import io.rala.shell.Input;
import io.rala.shell.exception.StopShellException;
import io.rala.shell.testUtils.TestContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ExitCommandTest {
    private static final String COMMAND = "exit";
    private static final String DOCUMENTATION = "stops shell";
    private static final String USAGE = "exit";

    @Test
    void documentation() {
        assertThat(new ExitCommand().getDocumentation()).isEqualTo(DOCUMENTATION);
    }

    @Test
    void usage() {
        assertThat(new ExitCommand().getUsage()).isEqualTo(USAGE);
    }

    @Test
    void executeWithoutParameters() {
        assertThatExceptionOfType(StopShellException.class).isThrownBy(() ->
            new ExitCommand().execute(new Input(COMMAND), new TestContext()));
    }

    @Test
    void executeWithParameters() {
        TestContext testContext = TestContext.getInstanceWithDifferentStreams();
        new ExitCommand().execute(new Input(COMMAND, "cmd"), testContext);
        assertThat(testContext.getOutputHistory()).isEmpty();
        assertThat(testContext.getErrorHistory()).isNotEmpty()
            .contains("error: no arguments expected");
    }

    @Test
    void toStringOfCommand() {
        assertThat(new ExitCommand()).hasToString("ExitCommand");
    }
}
