package io.rala.shell;

import io.rala.shell.command.Command;
import io.rala.shell.exception.CommandAlreadyPresentException;
import io.rala.shell.exception.StopShellException;
import io.rala.shell.testUtils.TestShell;
import io.rala.shell.testUtils.object.TestObject;
import io.rala.shell.testUtils.object.TestObjectWithAttributes;
import io.rala.shell.testUtils.object.TestObjectWithLocalDate;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static io.rala.shell.testUtils.WaitUtils.waitUntilNot;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

class ShellTest {
    private static final int TIMEOUT = 1;
    private static final Command ECHO_COMMAND = (input, context) ->
        context.printLine(String.join(" ", input.getArguments()));
    private static final Command EXIT_COMMAND = (input, context) -> {
        throw new StopShellException();
    };

    @Test
    void sameOutputs() {
        TestShell testShell = TestShell.getInstanceWithSameOutputs();
        Shell shell = testShell.getShell();
        shell.printLine("line");
        assertThat(testShell.getOutputHistory()).contains("line");
        shell.printError("error");
        assertThat(testShell.getOutputHistory()).contains("error");
    }

    @Test
    void differentOutputs() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.printLine("line");
        assertThat(testShell.getOutputHistory()).contains("line");
        assertThat(testShell.getErrorHistory()).doesNotContain("line");
        shell.printError("error");
        assertThat(testShell.getOutputHistory()).doesNotContain("error");
        assertThat(testShell.getErrorHistory()).contains("error");
    }

    @Test
    void input() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("echo", ECHO_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("echo echo");
            String take = testShell.getOutputHistory().take();
            assertThat(take).isNotNull().isEqualTo("> echo");
            assertThat(testShell.getErrorHistory()).isEmpty();
        });
        thread.interrupt();
    }

    @Test
    void prompt() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("echo", ECHO_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("echo echo");
            String take = testShell.getOutputHistory().take();
            assertThat(take).isNotNull().startsWith(Shell.DEFAULT_PROMPT);
            assertThat(testShell.getErrorHistory()).isEmpty();
        });

        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            shell.setPrompt("prompt> ");
            testShell.putLine("echo echo");
            String take = testShell.getOutputHistory().take();
            assertThat(take)
                .isNotNull()
                .startsWith(Shell.DEFAULT_PROMPT)
                .doesNotStartWith("prompt> ");
            assertThat(testShell.getErrorHistory()).isEmpty();

            testShell.putLine("echo echo");
            take = testShell.getOutputHistory().take();
            assertThat(take).isNotNull().startsWith("prompt> ");
            assertThat(testShell.getErrorHistory()).isEmpty();
        });
        thread.interrupt();
    }

    @Test
    void commandNotFound() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            assertThat(take).isEqualTo("command not found: close");
        });
        thread.interrupt();
    }

    @Test
    void fallbackCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.setFallback((input, context) -> context.printLine("fallback"));

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            assertThat(shell.isStopOnInvalidCommandEnabled()).isFalse();
            testShell.putLine("close");
            String output = testShell.getOutputHistory().take();
            assertThat(output).isEqualTo("> fallback");
            String error = testShell.getErrorHistory().peek();
            assertThat(error).isNotEqualTo("command not found: close");
        });
        thread.interrupt();
    }

    @Test
    void exceptionHandlerWithoutException() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObject());
        shell.setExceptionHandler((exception, context) ->
            context.printError("errorHandler")
        );

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("exceptionCommandWithoutMessage");
            String take = testShell.getErrorHistory().take();
            assertThat(take).isEqualTo("errorHandler");
        });
        assertThat(thread.isAlive()).isTrue();
        thread.interrupt();
    }

    @Test
    void exceptionHandlerThrowingException() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObject());
        shell.setExceptionHandler((exception, context) -> {
            throw new RuntimeException();
        });

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("exceptionCommandWithoutMessage");
            String take = testShell.getErrorHistory().take();
            assertThat(take)
                .isEqualTo("error in exception handler: java.lang.RuntimeException");
        });
        assertThat(thread.isAlive()).isTrue();
        thread.interrupt();
    }

    @Test
    void inputCloseClosesThread() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();

        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.closeInputStream();
            waitUntilNot(thread::isAlive);
        });

        assertThat(thread.isAlive()).isFalse();
        assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED);
    }

    @Test
    void threadInterruptClosesThread() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();

        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            thread.interrupt();
            waitUntilNot(thread::isAlive);
        });

        assertThat(thread.isAlive()).isFalse();
        assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED);
    }

    @Test
    void exit() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("exit", EXIT_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("exit");
            waitUntilNot(thread::isAlive);
        });
        assertThat(thread.isAlive()).isFalse();
        assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED);
    }

    @Test
    void registerExitDefault() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.EXIT);

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("exit");
            waitUntilNot(thread::isAlive);
        });

        assertThat(thread.isAlive()).isFalse();
        assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED);
    }

    @Test
    void registerHelpDefault() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.HELP);

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("help");
            String take = testShell.getOutputHistory().take();
            assertThat(take).isEqualTo("> help \tprints help of all commands or provided ones");
        });
        thread.interrupt();
    }

    @Test
    void registerMultipleDefaults() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.HELP, DefaultCommand.EXIT);

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("help");
            for (int i = 0; i < 2; i++)
                assertThat(testShell.getOutputHistory().take()).isNotEmpty();
        });
        thread.interrupt();
    }

    @Test
    void registerAllDefaults() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.values());

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("help");
            for (int i = 0; i < DefaultCommand.values().length; i++)
                assertThat(testShell.getOutputHistory().take()).isNotEmpty();
        });
        thread.interrupt();
    }

    @Test
    void registerDefaultCommandWhichIsAlreadyRegisteredByDefaultCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.HELP);
        assertThatThrownBy(() -> shell.register(DefaultCommand.HELP))
            .isInstanceOf(CommandAlreadyPresentException.class)
            .hasMessage("help");
    }

    @Test
    void registerObjectCommandWhichIsAlreadyRegisteredByManualCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("cmd", ECHO_COMMAND);
        assertThatThrownBy(() -> shell.register(new TestObjectWithAttributes()))
            .isInstanceOf(CommandAlreadyPresentException.class)
            .hasMessage("cmd");
    }

    @Test
    void registerManualCommandWhichIsAlreadyRegisteredByObjectCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObjectWithAttributes());
        assertThatThrownBy(() -> shell.register("cmd", ECHO_COMMAND))
            .isInstanceOf(CommandAlreadyPresentException.class)
            .hasMessage("cmd");
    }

    @Test
    void registerManualCommandWhichHasSpaceInName() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObjectWithAttributes());
        assertThatThrownBy(() -> shell.register("cmd with spaces", ECHO_COMMAND))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("no space allowed in command name: ")
            .hasMessageEndingWith("cmd with spaces");
    }

    @Test
    void addCustomStringMapperForLocalDate() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.addCustomStringMapper(LocalDate.class, LocalDate::parse);
        shell.register(new TestObjectWithLocalDate());

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("commandWithLocalDate 2018-11-25");
            assertThat(testShell.getOutputHistory().take())
                .isEqualTo("> 2018-11-25");
        });
        thread.interrupt();
    }

    @Test
    void exceptionCommandWithoutMessage() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObject());

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("exceptionCommandWithoutMessage");
            String take = testShell.getErrorHistory().take();
            assertThat(take).isEqualTo("error during execution: RuntimeException");
        });
        assertThat(thread.isAlive()).isTrue();
        thread.interrupt();
    }

    @Test
    void exceptionCommandWithMessage() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObject());

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            testShell.putLine("exceptionCommandWithMessage");
            String take = testShell.getErrorHistory().take();
            assertThat(take).isEqualTo("error during execution: RuntimeException: message");
        });
        assertThat(thread.isAlive()).isTrue();
        thread.interrupt();
    }

    @Test
    void stopOnInvalidCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        assertThat(thread.isAlive()).isTrue();
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            assertThat(shell.isStopOnInvalidCommandEnabled()).isFalse();
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            assertThat(take).isEqualTo("command not found: close");
        });
        assertThat(thread.isAlive()).isTrue();
        shell.setStopOnInvalidCommandEnabled(true);
        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            assertThat(shell.isStopOnInvalidCommandEnabled()).isTrue();
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            assertThat(take).isEqualTo("command not found: close");
            waitUntilNot(thread::isAlive);
        });
        assertThat(thread.isAlive()).isFalse();
        assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED);
    }

    @Test
    void toStringOfShellWithEmptyConstructor() {
        assertThat(new Shell()).hasToString("Shell");
    }
}
