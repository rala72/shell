package io.rala.shell;

import io.rala.StringMapper;
import io.rala.shell.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

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
    protected Context(
        @NotNull PrintWriter output,
        @NotNull Map<String, Command> commands
    ) {
        this(output, output, commands);
    }

    /**
     * @param output   output writer of shell
     * @param error    error writer of shell
     * @param commands commands of shell
     * @since 1.0.0
     */
    protected Context(
        @NotNull PrintWriter output, @NotNull PrintWriter error,
        @NotNull Map<String, Command> commands
    ) {
        this.output = output;
        this.error = error;
        this.commands = Collections.unmodifiableMap(commands);
    }

    /**
     * @param s string to print as line
     * @since 1.0.0
     */
    public void printLine(@NotNull String s) {
        getOutput().println(s);
    }

    /**
     * @param s string to print as error line
     * @since 1.0.0
     */
    public void printError(@NotNull String s) {
        getError().println(s);
    }

    /**
     * @return current supported commands
     * @since 1.0.0
     */
    @NotNull
    @Unmodifiable
    public Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * @return current {@link StringMapper}
     * @since 1.1.0
     */
    @NotNull
    public StringMapper getStringMapper() {
        return stringMapper;
    }

    /**
     * @param type   type of mapper
     * @param mapper custom mapper to consider
     * @param <T>    requested type
     * @param <R>    result type (may be super class of {@code T})
     * @see StringMapper#addCustomMapper(Class, Function)
     * @since 1.1.0
     * @deprecated prefer calling {@link #getStringMapper()} directly
     */
    @Deprecated(since = "1.1.2")
    public <T, R extends T> void addCustomStringMapper(
        @NotNull Class<T> type, @Nullable Function<String, R> mapper
    ) {
        if (mapper == null) getStringMapper().removeCustomMapper(type);
        else getStringMapper().addCustomMapper(type, mapper);
    }

    @Override
    @NotNull
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
    @NotNull
    protected PrintWriter getOutput() {
        return output;
    }

    /**
     * @return error
     * @since 1.0.0
     */
    @NotNull
    protected PrintWriter getError() {
        return error;
    }
}
