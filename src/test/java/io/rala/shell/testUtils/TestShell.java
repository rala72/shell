package io.rala.shell.testUtils;

import io.rala.shell.Shell;
import io.rala.shell.testUtils.io.BlockingQueueInputStream;
import io.rala.shell.testUtils.io.HistoryOutputStream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings("unused")
public class TestShell {
    private final Shell shell;
    private final BlockingQueueInputStream inputStream;
    private final BlockingQueue<String> queue;
    private final BlockingQueue<String> outputHistory;
    private final BlockingQueue<String> errorHistory;

    private TestShell(HistoryOutputStream outputStream) {
        queue = new LinkedBlockingQueue<>();
        inputStream = new BlockingQueueInputStream(queue);
        shell = new Shell(inputStream, outputStream);
        outputHistory = outputStream.getHistory();
        errorHistory = outputHistory;
    }

    private TestShell(HistoryOutputStream outputStream, HistoryOutputStream errorStream) {
        queue = new LinkedBlockingQueue<>();
        inputStream = new BlockingQueueInputStream(queue);
        shell = new Shell(inputStream, outputStream, errorStream);
        outputHistory = outputStream.getHistory();
        errorHistory = errorStream.getHistory();
    }

    public void requestIoException() {
        inputStream.requestIoException();
    }

    public void closeInputStream() {
        inputStream.close();
    }

    public void put(String s) throws InterruptedException {
        queue.put(s);
    }

    public void putLine(String s) throws InterruptedException {
        put(s + "\n");
    }

    public Shell getShell() {
        return shell;
    }

    public BlockingQueue<String> getOutputHistory() {
        return outputHistory;
    }

    public BlockingQueue<String> getErrorHistory() {
        return errorHistory;
    }

    public static TestShell getInstanceWithSameOutputs() {
        return new TestShell(new HistoryOutputStream());
    }

    public static TestShell getInstanceWithDifferentOutputs() {
        return new TestShell(new HistoryOutputStream(), new HistoryOutputStream());
    }
}
