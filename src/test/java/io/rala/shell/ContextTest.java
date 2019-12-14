package io.rala.shell;

import io.rala.shell.command.ExitCommand;
import io.rala.shell.testUtils.TestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

class ContextTest {
    @Test
    void emptyCommands() {
        Context context = new Context(getSystemOutPrintWriter(), Collections.emptyMap());
        Assertions.assertTrue(context.getCommands().isEmpty());
    }

    @Test
    void constructorWithSameOutputs() {
        Context context = new Context(
            getSystemOutPrintWriter(),
            Collections.emptyMap()
        );
        Assertions.assertEquals(context.getOutput(), context.getError());
    }

    @Test
    void constructorWithDifferentOutputs() {
        Context context = new Context(
            getSystemOutPrintWriter(),
            getSystemErrorPrintWriter(),
            Collections.emptyMap()
        );
        Assertions.assertNotEquals(context.getOutput(), context.getError());
    }

    @Test
    void commandsWithExit() {
        Context context = new Context(getSystemOutPrintWriter(), Map.of("help", new ExitCommand()));
        Assertions.assertFalse(context.getCommands().isEmpty());
        Assertions.assertTrue(context.getCommands().containsKey("help"));
        Assertions.assertNotNull(context.getCommands().get("help"));
    }

    @Test
    void outputAndError() {
        TestContext testContext = new TestContext();

        testContext.printLine("line");

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertFalse(outputs.isEmpty());
        Assertions.assertEquals(1, outputs.size());
        Assertions.assertTrue(outputs.contains("line"));

        testContext.printError("error");

        BlockingQueue<String> errors = testContext.getErrorHistory();
        Assertions.assertEquals(outputs, errors);
    }

    @Test
    void errorButNotOutput() {
        TestContext testContext = TestContext.getInstanceWithDifferentStreams();

        testContext.printError("error");

        BlockingQueue<String> errors = testContext.getErrorHistory();
        Assertions.assertFalse(errors.isEmpty());
        Assertions.assertEquals(1, errors.size());
        Assertions.assertTrue(errors.contains("error"));

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        Assertions.assertTrue(outputs.isEmpty());
        Assertions.assertEquals(0, outputs.size());
        Assertions.assertFalse(outputs.contains("error"));
    }

    @Test
    void toStringOfEmptyContext() {
        String toString = "Context{output==error=false, commands={}}";
        Assertions.assertEquals(toString, new TestContext().toString());
    }

    private static PrintWriter getSystemOutPrintWriter() {
        return getPrintWriteOfOutputStream(System.out);
    }

    private static PrintWriter getSystemErrorPrintWriter() {
        return getPrintWriteOfOutputStream(System.err);
    }

    private static PrintWriter getPrintWriteOfOutputStream(OutputStream outputStream) {
        return new PrintWriter(new OutputStreamWriter(outputStream));
    }
}
