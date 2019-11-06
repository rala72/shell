package io.rala.shell;

import io.rala.shell.command.Command;
import io.rala.shell.exception.CommandAlreadyPresentException;
import io.rala.shell.exception.StopShellException;
import io.rala.shell.utils.TestObject;
import io.rala.shell.utils.TestObjects;
import io.rala.shell.utils.TestShell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static io.rala.shell.utils.WaitUtils.waitUntilNot;

class ShellTest {
    private static final int TIMEOUT = 5;
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
        Assertions.assertTrue(testShell.getOutputHistory().contains("line"));
        shell.printError("error");
        Assertions.assertTrue(testShell.getOutputHistory().contains("error"));
    }

    @Test
    void differentOutputs() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.printLine("line");
        Assertions.assertTrue(testShell.getOutputHistory().contains("line"));
        Assertions.assertFalse(testShell.getErrorHistory().contains("line"));
        shell.printError("error");
        Assertions.assertFalse(testShell.getOutputHistory().contains("error"));
        Assertions.assertTrue(testShell.getErrorHistory().contains("error"));
    }

    @Test
    void input() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("echo", ECHO_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("echo echo");
            String take = testShell.getOutputHistory().take();
            Assertions.assertNotNull(take);
            Assertions.assertEquals("> echo", take);
            Assertions.assertTrue(testShell.getErrorHistory().isEmpty());
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
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("echo echo");
            String take = testShell.getOutputHistory().take();
            Assertions.assertNotNull(take);
            Assertions.assertTrue(take.startsWith(Shell.DEFAULT_PROMPT));
            Assertions.assertTrue(testShell.getErrorHistory().isEmpty());
        });

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            shell.setPrompt("prompt> ");
            testShell.putLine("echo echo");
            String take = testShell.getOutputHistory().take();
            Assertions.assertNotNull(take);
            Assertions.assertTrue(take.startsWith(Shell.DEFAULT_PROMPT));
            Assertions.assertFalse(take.startsWith("prompt> "));
            Assertions.assertTrue(testShell.getErrorHistory().isEmpty());

            testShell.putLine("echo echo");
            take = testShell.getOutputHistory().take();
            Assertions.assertNotNull(take);
            Assertions.assertTrue(take.startsWith("prompt> "));
            Assertions.assertTrue(testShell.getErrorHistory().isEmpty());
        });
        thread.interrupt();
    }

    @Test
    void commandNotFound() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            Assertions.assertEquals("command not found: close", take);
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
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            Assertions.assertFalse(shell.isStopOnInvalidCommandEnabled());
            testShell.putLine("close");
            String output = testShell.getOutputHistory().take();
            Assertions.assertEquals("> fallback", output);
            String error = testShell.getErrorHistory().peek();
            Assertions.assertNotEquals("command not found: close", error);
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
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exceptionCommandWithoutMessage");
            String take = testShell.getErrorHistory().take();
            Assertions.assertEquals("errorHandler", take);
        });
        Assertions.assertTrue(thread.isAlive());
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
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exceptionCommandWithoutMessage");
            String take = testShell.getErrorHistory().take();
            Assertions.assertEquals(
                "error in exception handler: java.lang.RuntimeException",
                take
            );
        });
        Assertions.assertTrue(thread.isAlive());
        thread.interrupt();
    }

    @Test
    void inputCloseClosesThread() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.closeInputStream();
            waitUntilNot(thread::isAlive);
        });

        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void threadInterruptClosesThread() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            thread.interrupt();
            waitUntilNot(thread::isAlive);
        });

        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void exit() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("exit", EXIT_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exit");
            waitUntilNot(thread::isAlive);
        });
        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void registerExitDefault() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.EXIT);

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exit");
            waitUntilNot(thread::isAlive);
        });

        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void registerHelpDefault() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.HELP);

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("help");
            String take = testShell.getOutputHistory().take();
            Assertions.assertEquals("> help \tprints help of all commands or provided ones", take);
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
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("help");
            for (int i = 0; i < 2; i++)
                Assertions.assertFalse(testShell.getOutputHistory().take().isEmpty());
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
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("help");
            for (int i = 0; i < DefaultCommand.values().length; i++)
                Assertions.assertFalse(testShell.getOutputHistory().take().isEmpty());
        });
        thread.interrupt();
    }

    @Test
    void registerDefaultCommandWhichIsAlreadyRegisteredByDefaultCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.HELP);
        try {
            shell.register(DefaultCommand.HELP);
        } catch (CommandAlreadyPresentException e) {
            Assertions.assertEquals("help", e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void registerObjectCommandWhichIsAlreadyRegisteredByManualCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("cmd", ECHO_COMMAND);
        try {
            shell.register(new TestObjects.TestObjectWithAttributes());
        } catch (CommandAlreadyPresentException e) {
            Assertions.assertEquals("cmd", e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void registerManualCommandWhichIsAlreadyRegisteredByObjectCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObjects.TestObjectWithAttributes());
        try {
            shell.register("cmd", ECHO_COMMAND);
        } catch (CommandAlreadyPresentException e) {
            Assertions.assertEquals("cmd", e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void exceptionCommandWithoutMessage() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObject());

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exceptionCommandWithoutMessage");
            String take = testShell.getErrorHistory().take();
            Assertions.assertEquals(
                "error during execution: RuntimeException", take
            );
        });
        Assertions.assertTrue(thread.isAlive());
        thread.interrupt();
    }

    @Test
    void exceptionCommandWithMessage() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObject());

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exceptionCommandWithMessage");
            String take = testShell.getErrorHistory().take();
            Assertions.assertEquals(
                "error during execution: RuntimeException: message", take
            );
        });
        Assertions.assertTrue(thread.isAlive());
        thread.interrupt();
    }

    @Test
    void stopOnInvalidCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTrue(thread.isAlive());
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            Assertions.assertFalse(shell.isStopOnInvalidCommandEnabled());
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            Assertions.assertEquals("command not found: close", take);
        });
        Assertions.assertTrue(thread.isAlive());
        shell.setStopOnInvalidCommandEnabled(true);
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            Assertions.assertTrue(shell.isStopOnInvalidCommandEnabled());
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            Assertions.assertEquals("command not found: close", take);
            waitUntilNot(thread::isAlive);
        });
        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void toStringOfShellWithEmptyConstructor() {
        Assertions.assertEquals("Shell", new Shell().toString());
    }
}
