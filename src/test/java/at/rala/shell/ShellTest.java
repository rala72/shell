package at.rala.shell;

import at.rala.shell.command.Command;
import at.rala.shell.utils.io.BlockingQueueInputStream;
import at.rala.shell.utils.io.CacheOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ShellTest {
    private static final Command ECHO_COMMAND = (input, context) ->
        context.printLine(String.join(" ", input.getArguments()));

    @Test
    void testShellPrintln() {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        CacheOutputStream outputStream = new CacheOutputStream();
        CacheOutputStream errorStream = new CacheOutputStream();
        Shell shell = new Shell(
            new BlockingQueueInputStream(queue),
            outputStream,
            errorStream
        );
        shell.printLine("line");
        Assertions.assertTrue(outputStream.getOutputs().contains("line"));
        Assertions.assertFalse(errorStream.getOutputs().contains("line"));
        shell.printError("error");
        Assertions.assertFalse(outputStream.getOutputs().contains("error"));
        Assertions.assertTrue(errorStream.getOutputs().contains("error"));
    }

    @Test
    void testShellInput() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        CacheOutputStream outputStream = new CacheOutputStream();
        CacheOutputStream errorStream = new CacheOutputStream();
        Shell shell = new Shell(
            new BlockingQueueInputStream(queue),
            outputStream,
            errorStream
        );
        shell.register("echo", ECHO_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        queue.put("echo echo\n");
        String take = outputStream.getOutputs().take();
        Assertions.assertNotNull(take);
        Assertions.assertEquals("> echo", take);
        Assertions.assertTrue(errorStream.getOutputs().isEmpty());
        thread.interrupt();
    }

    @Test
    void testToString() {
        Assertions.assertEquals("Shell", new Shell().toString());
    }
}
