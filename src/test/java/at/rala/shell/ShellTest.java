package at.rala.shell;

import at.rala.shell.command.Command;
import at.rala.shell.utils.io.BlockingQueueInputStream;
import at.rala.shell.utils.io.HistoryOutputStream;
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
        HistoryOutputStream outputStream = new HistoryOutputStream();
        HistoryOutputStream errorStream = new HistoryOutputStream();
        Shell shell = new Shell(
            new BlockingQueueInputStream(queue),
            outputStream,
            errorStream
        );
        shell.printLine("line");
        Assertions.assertTrue(outputStream.getHistory().contains("line"));
        Assertions.assertFalse(errorStream.getHistory().contains("line"));
        shell.printError("error");
        Assertions.assertFalse(outputStream.getHistory().contains("error"));
        Assertions.assertTrue(errorStream.getHistory().contains("error"));
    }

    @Test
    void testShellInput() throws InterruptedException {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        HistoryOutputStream outputStream = new HistoryOutputStream();
        HistoryOutputStream errorStream = new HistoryOutputStream();
        Shell shell = new Shell(
            new BlockingQueueInputStream(queue),
            outputStream,
            errorStream
        );
        shell.register("echo", ECHO_COMMAND);

        Thread thread = new Thread(shell);
        thread.start();
        queue.put("echo echo\n");
        String take = outputStream.getHistory().take();
        Assertions.assertNotNull(take);
        Assertions.assertEquals("> echo", take);
        Assertions.assertTrue(errorStream.getHistory().isEmpty());
        thread.interrupt();
    }

    @Test
    void testToString() {
        Assertions.assertEquals("Shell", new Shell().toString());
    }
}
