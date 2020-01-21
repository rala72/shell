package io.rala.shell;

import io.rala.shell.command.Command;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

/**
 * context of shell which holds output, error and commands
 */
public class Context {
    private final PrintWriter output;
    private final PrintWriter error;
    private final Map<String, Command> commands;

    /**
     * error writer will be the same as output
     *
     * @param output   output writer of shell
     * @param commands commands of shell
     * @see #Context(PrintWriter, PrintWriter, Map)
     */
    public Context(PrintWriter output, Map<String, Command> commands) {
        this(output, output, commands);
    }

    /**
     * @param output   output writer of shell
     * @param error    error writer of shell
     * @param commands commands of shell
     */
    public Context(PrintWriter output, PrintWriter error, Map<String, Command> commands) {
        this.output = output;
        this.error = error;
        this.commands = Collections.unmodifiableMap(commands);
    }

    /**
     * @param s string to print as line
     */
    public void printLine(String s) {
        getOutput().println(s);
    }

    /**
     * @param s string to print as error line
     */
    public void printError(String s) {
        getError().println(s);
    }

    /**
     * @return current supported commands
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    @Override
    public String toString() {
        return "Context{" +
            "output==error=" + (output == error) +
            ", commands=" + commands +
            '}';
    }

    /**
     * @return output
     */
    protected PrintWriter getOutput() {
        return output;
    }

    /**
     * @return error
     */
    protected PrintWriter getError() {
        return error != null ? error : output;
    }
}
