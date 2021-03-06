package io.rala.shell.testUtils;

import io.rala.shell.Context;
import io.rala.shell.command.Command;
import io.rala.shell.testUtils.io.HistoryOutputStream;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class TestContext extends Context {
    private final BlockingQueue<String> outputHistory;
    private final BlockingQueue<String> errorHistory;

    public TestContext() {
        this(new HistoryOutputStream());
    }

    public TestContext(Map<String, Command> commands) {
        this(new HistoryOutputStream(), commands);
    }

    public TestContext(HistoryOutputStream outputStream) {
        this(outputStream, outputStream);
    }

    public TestContext(HistoryOutputStream outputStream, Map<String, Command> commands) {
        this(outputStream, outputStream, commands);
    }

    public TestContext(HistoryOutputStream outputStream, HistoryOutputStream errorStream) {
        this(outputStream, errorStream, null);
    }

    public TestContext(HistoryOutputStream outputStream, HistoryOutputStream errorStream,
                       Map<String, Command> commands) {
        super(
            new PrintWriter(outputStream, true),
            new PrintWriter(errorStream, true),
            commands != null ? commands : Collections.emptyMap()
        );
        this.outputHistory = outputStream.getHistory();
        this.errorHistory = errorStream.getHistory();
    }

    public static TestContext getInstanceWithDifferentStreams() {
        return getInstanceWithDifferentStreams(Collections.emptyMap());
    }

    public static TestContext getInstanceWithDifferentStreams(Map<String, Command> commands) {
        return new TestContext(new HistoryOutputStream(), new HistoryOutputStream(), commands);
    }

    public BlockingQueue<String> getOutputHistory() {
        return outputHistory;
    }

    public BlockingQueue<String> getErrorHistory() {
        return errorHistory;
    }
}
