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
import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(testShell.getOutputHistory().contains("line"));
        shell.printError("error");
        assertTrue(testShell.getOutputHistory().contains("error"));
    }

    @Test
    void differentOutputs() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.printLine("line");
        assertTrue(testShell.getOutputHistory().contains("line"));
        assertFalse(testShell.getErrorHistory().contains("line"));
        shell.printError("error");
        assertFalse(testShell.getOutputHistory().contains("error"));
        assertTrue(testShell.getErrorHistory().contains("error"));
    }

    @Test
    void input() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("echo", ECHO_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("echo echo");
            String take = testShell.getOutputHistory().take();
            assertNotNull(take);
            assertEquals("> echo", take);
            assertTrue(testShell.getErrorHistory().isEmpty());
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
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("echo echo");
            String take = testShell.getOutputHistory().take();
            assertNotNull(take);
            assertTrue(take.startsWith(Shell.DEFAULT_PROMPT));
            assertTrue(testShell.getErrorHistory().isEmpty());
        });

        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            shell.setPrompt("prompt> ");
            testShell.putLine("echo echo");
            String take = testShell.getOutputHistory().take();
            assertNotNull(take);
            assertTrue(take.startsWith(Shell.DEFAULT_PROMPT));
            assertFalse(take.startsWith("prompt> "));
            assertTrue(testShell.getErrorHistory().isEmpty());

            testShell.putLine("echo echo");
            take = testShell.getOutputHistory().take();
            assertNotNull(take);
            assertTrue(take.startsWith("prompt> "));
            assertTrue(testShell.getErrorHistory().isEmpty());
        });
        thread.interrupt();
    }

    @Test
    void commandNotFound() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            assertEquals("command not found: close", take);
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
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            assertFalse(shell.isStopOnInvalidCommandEnabled());
            testShell.putLine("close");
            String output = testShell.getOutputHistory().take();
            assertEquals("> fallback", output);
            String error = testShell.getErrorHistory().peek();
            assertNotEquals("command not found: close", error);
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
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exceptionCommandWithoutMessage");
            String take = testShell.getErrorHistory().take();
            assertEquals("errorHandler", take);
        });
        assertTrue(thread.isAlive());
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
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exceptionCommandWithoutMessage");
            String take = testShell.getErrorHistory().take();
            assertEquals(
                "error in exception handler: java.lang.RuntimeException",
                take
            );
        });
        assertTrue(thread.isAlive());
        thread.interrupt();
    }

    @Test
    void inputCloseClosesThread() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());

        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.closeInputStream();
            waitUntilNot(thread::isAlive);
        });

        assertFalse(thread.isAlive());
        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void threadInterruptClosesThread() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());

        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            thread.interrupt();
            waitUntilNot(thread::isAlive);
        });

        assertFalse(thread.isAlive());
        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void exit() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("exit", EXIT_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exit");
            waitUntilNot(thread::isAlive);
        });
        assertFalse(thread.isAlive());
        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void registerExitDefault() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.EXIT);

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exit");
            waitUntilNot(thread::isAlive);
        });

        assertFalse(thread.isAlive());
        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void registerHelpDefault() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(DefaultCommand.HELP);

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("help");
            String take = testShell.getOutputHistory().take();
            assertEquals("> help \tprints help of all commands or provided ones", take);
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
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("help");
            for (int i = 0; i < 2; i++)
                assertFalse(testShell.getOutputHistory().take().isEmpty());
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
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("help");
            for (int i = 0; i < DefaultCommand.values().length; i++)
                assertFalse(testShell.getOutputHistory().take().isEmpty());
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
            fail();
        } catch (CommandAlreadyPresentException e) {
            assertEquals("help", e.getMessage());
        }
    }

    @Test
    void registerObjectCommandWhichIsAlreadyRegisteredByManualCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("cmd", ECHO_COMMAND);
        try {
            shell.register(new TestObjectWithAttributes());
            fail();
        } catch (CommandAlreadyPresentException e) {
            assertEquals("cmd", e.getMessage());
        }
    }

    @Test
    void registerManualCommandWhichIsAlreadyRegisteredByObjectCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObjectWithAttributes());
        try {
            shell.register("cmd", ECHO_COMMAND);
            fail();
        } catch (CommandAlreadyPresentException e) {
            assertEquals("cmd", e.getMessage());
        }
    }

    @Test
    void registerManualCommandWhichHasSpaceInName() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObjectWithAttributes());
        try {
            shell.register("cmd with spaces", ECHO_COMMAND);
            fail();
        } catch (IllegalArgumentException e) {
            final String prefix = "no space allowed in command name: ";
            assertEquals(prefix + "cmd with spaces", e.getMessage());
        }
    }

    @Test
    void addCustomStringMapperForLocalDate() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.addCustomStringMapper(LocalDate.class, LocalDate::parse);
        shell.register(new TestObjectWithLocalDate());

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("commandWithLocalDate 2018-11-25");
            assertEquals("> 2018-11-25",
                testShell.getOutputHistory().take()
            );
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
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exceptionCommandWithoutMessage");
            String take = testShell.getErrorHistory().take();
            assertEquals(
                "error during execution: RuntimeException", take
            );
        });
        assertTrue(thread.isAlive());
        thread.interrupt();
    }

    @Test
    void exceptionCommandWithMessage() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register(new TestObject());

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            testShell.putLine("exceptionCommandWithMessage");
            String take = testShell.getErrorHistory().take();
            assertEquals(
                "error during execution: RuntimeException: message", take
            );
        });
        assertTrue(thread.isAlive());
        thread.interrupt();
    }

    @Test
    void stopOnInvalidCommand() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        assertTrue(thread.isAlive());
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            assertFalse(shell.isStopOnInvalidCommandEnabled());
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            assertEquals("command not found: close", take);
        });
        assertTrue(thread.isAlive());
        shell.setStopOnInvalidCommandEnabled(true);
        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            assertTrue(shell.isStopOnInvalidCommandEnabled());
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            assertEquals("command not found: close", take);
            waitUntilNot(thread::isAlive);
        });
        assertFalse(thread.isAlive());
        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void toStringOfShellWithEmptyConstructor() {
        assertEquals("Shell", new Shell().toString());
    }
}
