package at.rala.shell;

import at.rala.shell.command.Command;

import java.io.PrintWriter;
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
        this.commands = commands;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public PrintWriter getError() {
        return error != null ? error : output;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}