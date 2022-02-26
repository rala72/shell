package io.rala.shell;

import io.rala.shell.command.ExitCommand;
import io.rala.shell.testUtils.TestContext;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

class ContextTest {
    @Test
    void emptyCommands() {
        Context context = new Context(getSystemOutPrintWriter(), Collections.emptyMap());
        assertThat(context.getCommands()).isEmpty();
    }

    @Test
    void constructorWithSameOutputs() {
        Context context = new Context(
            getSystemOutPrintWriter(),
            Collections.emptyMap()
        );
        assertThat(context.getError()).isEqualTo(context.getOutput());
    }

    @Test
    void constructorWithDifferentOutputs() {
        Context context = new Context(
            getSystemOutPrintWriter(),
            getSystemErrorPrintWriter(),
            Collections.emptyMap()
        );
        assertThat(context.getError()).isNotEqualTo(context.getOutput());
    }

    @Test
    void commandsWithExit() {
        Context context = new Context(getSystemOutPrintWriter(), Map.of("help", new ExitCommand()));
        assertThat(context.getCommands())
            .isNotEmpty().containsKey("help");
    }

    @Test
    void outputAndError() {
        TestContext testContext = new TestContext();

        testContext.printLine("line");

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs)
            .isNotEmpty()
            .contains("line");

        testContext.printError("error");

        BlockingQueue<String> errors = testContext.getErrorHistory();
        assertThat(errors).isEqualTo(outputs);
    }

    @Test
    void errorButNotOutput() {
        TestContext testContext = TestContext.getInstanceWithDifferentStreams();

        testContext.printError("error");

        BlockingQueue<String> errors = testContext.getErrorHistory();
        assertThat(errors)
            .hasSize(1)
            .contains("error");

        BlockingQueue<String> outputs = testContext.getOutputHistory();
        assertThat(outputs).isEmpty();
    }

    @Test
    void toStringOfEmptyContext() {
        String toString = "Context{output==error=false, commands={}}";
        assertThat(new TestContext()).hasToString(toString);
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
