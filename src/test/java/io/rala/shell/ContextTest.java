package io.rala.shell;

import io.rala.shell.command.ExitCommand;
import io.rala.shell.utils.TestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@SuppressWarnings("unused")
class ContextTest {
    @Test
    void testEmptyCommands() {
        Context context = new Context(getSystemOutPrintWriter(), Collections.emptyMap());
        Assertions.assertTrue(context.getCommands().isEmpty());
    }

    @Test
    void testConstructorWithSameOutputs() {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out));
        Context context = new Context(printWriter, Collections.emptyMap());
        Assertions.assertEquals(context.getOutput(), context.getError());
    }

    @Test
    void testCommandsWithExit() {
        Context context = new Context(getSystemOutPrintWriter(), Map.of("help", new ExitCommand()));
        Assertions.assertFalse(context.getCommands().isEmpty());
        Assertions.assertTrue(context.getCommands().containsKey("help"));
        Assertions.assertNotNull(context.getCommands().get("help"));
    }

    @Test
    void testOutputAndError() {
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
    void testErrorButNotOutput() {
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
    void testToString() {
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