package io.rala.shell;

import io.rala.StringMapper;
import io.rala.shell.command.Command;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * context of shell which holds output, error and commands
 *
 * @since 1.0.0
 */
public class Context {
    private final PrintWriter output;
    private final PrintWriter error;
    private final Map<String, Command> commands;
    private final StringMapper stringMapper = new StringMapper();

    /**
     * error writer will be the same as output
     *
     * @param output   output writer of shell
     * @param commands commands of shell
     * @see #Context(PrintWriter, PrintWriter, Map)
     * @since 1.0.0
     */
    protected Context(PrintWriter output, Map<String, Command> commands) {
        this(output, output, commands);
    }

    /**
     * @param output   output writer of shell
     * @param error    error writer of shell
     * @param commands commands of shell
     * @since 1.0.0
     */
    protected Context(PrintWriter output, PrintWriter error, Map<String, Command> commands) {
        this.output = output;
        this.error = error;
        this.commands = Collections.unmodifiableMap(commands);
    }

    /**
     * @param s string to print as line
     * @since 1.0.0
     */
    public void printLine(String s) {
        getOutput().println(s);
    }

    /**
     * @param s string to print as error line
     * @since 1.0.0
     */
    public void printError(String s) {
        getError().println(s);
    }

    /**
     * @return current supported commands
     * @since 1.0.0
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * @return current StringMapper
     */
    public StringMapper getStringMapper() {
        return stringMapper;
    }

    /**
     * @param type   type of mapper
     * @param mapper custom mapper to consider
     * @param <T>    requested type
     * @param <R>    result type (may be super class of {@code T})
     * @see StringMapper#addCustomMapper(Class, Function)
     * @since 1.0.1
     */
    public <T, R extends T> void addCustomStringMapper(Class<T> type, Function<String, R> mapper) {
        if (mapper == null) getStringMapper().removeCustomMapper(type);
        else getStringMapper().addCustomMapper(type, mapper);
    }

    @Override
    public String toString() {
        return "Context{" +
            "output==error=" + (getOutput() == getError()) +
            ", commands=" + getCommands() +
            '}';
    }

    /**
     * @return output
     * @since 1.0.0
     */
    protected PrintWriter getOutput() {
        return output;
    }

    /**
     * @return error
     * @since 1.0.0
     */
    protected PrintWriter getError() {
        return error != null ? error : output;
    }
}
