package at.rala.shell;

import at.rala.shell.command.Command;
import at.rala.shell.exception.StopShellException;
import at.rala.shell.utils.TestShell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

class ShellTest {
    private static final int TIMEOUT = 5;
    private static final Command ECHO_COMMAND = (input, context) ->
        context.printLine(String.join(" ", input.getArguments()));
    private static final Command EXIT_COMMAND = (input, context) -> {
        throw new StopShellException();
    };

    @Test
    void testSameOutputs() {
        TestShell testShell = TestShell.getInstanceWithSameOutputs();
        Shell shell = testShell.getShell();
        shell.printLine("line");
        Assertions.assertTrue(testShell.getOutputHistory().contains("line"));
        shell.printError("error");
        Assertions.assertTrue(testShell.getOutputHistory().contains("error"));
    }

    @Test
    void testDifferentOutputs() {
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
    void testInput() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("echo", ECHO_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
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
    void testPrompt() {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("echo", ECHO_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
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
    void testExit() throws InterruptedException {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();
        shell.register("exit", EXIT_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT),
            () -> testShell.putLine("exit")
        );
        Thread.sleep(100);
        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void testStopOnInvalidCommand() throws InterruptedException {
        TestShell testShell = TestShell.getInstanceWithDifferentOutputs();
        Shell shell = testShell.getShell();

        Thread thread = new Thread(shell);
        thread.start();
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            Assertions.assertFalse(shell.isStopOnInvalidCommandEnabled());
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            Assertions.assertEquals("command not found: close", take);
        });
        Thread.sleep(100);
        Assertions.assertTrue(thread.isAlive());
        shell.setStopOnInvalidCommandEnabled(true);
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            Assertions.assertTrue(shell.isStopOnInvalidCommandEnabled());
            testShell.putLine("close");
            String take = testShell.getErrorHistory().take();
            Assertions.assertEquals("command not found: close", take);
        });
        Thread.sleep(100);
        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void testToString() {
        Assertions.assertEquals("Shell", new Shell().toString());
    }
}
