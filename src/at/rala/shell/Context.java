package at.rala.shell;

import at.rala.shell.command.Command;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Context {
    private final PrintWriter output;
    private final PrintWriter error;
    private final Map<String, Command> commands;

    public Context(PrintWriter output, Map<String, Command> commands) {
        this(output, output, commands);
    }

    public Context(PrintWriter output, PrintWriter error, Map<String, Command> commands) {
        this.output = output;
        this.error = error;
        this.commands = Collections.unmodifiableMap(commands);
    }

    public void printLine(String s) {
        getOutput().println(s);
    }

    public void printError(String s) {
        getError().println(s);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    @Override
    public String toString() {
        return "Context{" +
            "output=" + output +
            ", error=" + error +
            ", commands=" + commands +
            '}';
    }

    protected PrintWriter getOutput() {
        return output;
    }

    protected PrintWriter getError() {
        return error != null ? error : output;
    }
}
