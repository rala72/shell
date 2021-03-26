package io.rala.shell;

import io.rala.StringMapper;
import io.rala.shell.annotation.CommandLoader;
import io.rala.shell.command.Command;
import io.rala.shell.exception.CommandAlreadyPresentException;
import io.rala.shell.exception.ExceptionHandler;
import io.rala.shell.exception.MethodCallException;
import io.rala.shell.exception.StopShellException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * shell which calls methods based on input commands
 *
 * @since 1.0.0
 */
public class Shell implements Runnable {
    public static final String DEFAULT_PROMPT = "> ";

    private final ReaderQueue readerQueue;
    private final Context context;
    private final Map<String, Command> commands = new TreeMap<>();
    private Command fallback;
    private ExceptionHandler exceptionHandler;
    private String prompt = DEFAULT_PROMPT;
    private boolean isStopOnInvalidCommandEnabled = false;

    /**
     * new shell based on {@link System} streams
     *
     * @see #Shell(InputStream, OutputStream, OutputStream)
     * @since 1.0.0
     */
    public Shell() {
        this(System.in, System.out, System.err);
    }

    /**
     * {@code errorStream} is equals to {@code outputStream}
     *
     * @param inputStream  inputStream of shell
     * @param outputStream outputStream of shell
     * @since 1.0.0
     */
    public Shell(InputStream inputStream, OutputStream outputStream) {
        this(inputStream, outputStream, outputStream);
    }

    /**
     * @param inputStream  inputStream of shell
     * @param outputStream outputStream of shell
     * @param errorStream  errorStream of shell
     * @since 1.0.0
     */
    public Shell(InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        this.readerQueue = new ReaderQueue(reader);
        this.context = new Context(
            new PrintWriter(outputStream, true),
            new PrintWriter(errorStream, true),
            commands
        );
    }

    @Override
    public void run() {
        //noinspection InstantiatingAThreadWithDefaultRunMethod
        Thread thread = new Thread();
        try {
            thread = new Thread(readerQueue);
            thread.start();
            out:
            while (isRunning()) {
                printPrompt();
                String line = readerQueue.peek();
                while (line == null) {
                    if (!isRunning() || !isThreadRunning(thread)) break out;
                    line = readerQueue.peek();
                }
                if (!isRunning() || !isThreadRunning(thread)) break;
                line = readerQueue.take();
                if (line == null || line.isBlank()) continue;
                boolean success = handleInput(Input.parse(line));
                if (isStopOnInvalidCommandEnabled() && !success) break;
            }
        } catch (StopShellException ignored) {
        }
        thread.interrupt();
    }

    /**
     * @param fallback fallback command if none is found
     * @since 1.0.0
     */
    public void setFallback(Command fallback) {
        this.fallback = fallback;
    }

    /**
     * @param exceptionHandler exceptionHandler to customize exception handling
     * @since 1.0.0
     */
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * @param prompt prompt to show before user input
     * @since 1.0.0
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt != null ? prompt : DEFAULT_PROMPT;
    }

    /**
     * @return {@code true} if enabled
     * @since 1.0.0
     */
    public boolean isStopOnInvalidCommandEnabled() {
        return isStopOnInvalidCommandEnabled;
    }

    /**
     * @param stopOnInvalidCommandEnabled {@code true} if invalid command should stop shell
     * @since 1.0.0
     */
    public void setStopOnInvalidCommandEnabled(boolean stopOnInvalidCommandEnabled) {
        isStopOnInvalidCommandEnabled = stopOnInvalidCommandEnabled;
    }

    /**
     * @param object object to register all {@link io.rala.shell.annotation.Command} annotations
     * @see CommandLoader#CommandLoader(Object, StringMapper)
     * @see #register(String, Command)
     * @since 1.0.0
     */
    public void register(Object object) {
        new CommandLoader(object, context.getStringMapper()).getCommandMethodMap()
            .forEach(this::register);
    }

    /**
     * @param command command to register
     * @since 1.0.0
     */
    public void register(DefaultCommand command) {
        register(command.getName(), command.getCommand());
    }

    /**
     * @param command         command to register
     * @param furtherCommands further commands to register
     * @since 1.0.0
     */
    public void register(DefaultCommand command, DefaultCommand... furtherCommands) {
        if (command != null) register(command);
        register(furtherCommands);
    }

    /**
     * @param commands commands to register
     * @since 1.0.0
     */
    public void register(DefaultCommand[] commands) {
        for (DefaultCommand command : commands)
            register(command);
    }

    /**
     * @param name    name of command to register
     * @param command command implementation to register
     * @throws CommandAlreadyPresentException if command is already present
     * @throws IllegalArgumentException       if {@code name} contains space
     * @since 1.0.0
     */
    public void register(String name, Command command) {
        if (commands.containsKey(name))
            throw new CommandAlreadyPresentException(name);
        if (name.contains(" "))
            throw new IllegalArgumentException("no space allowed in command name: " + name);
        commands.put(name, command);
    }

    /**
     * @param type   type of mapper
     * @param mapper custom mapper to consider
     * @param <T>    requested type
     * @param <R>    result type (may be super class of {@code T})
     * @see Context#addCustomStringMapper(Class, Function)
     * @see StringMapper#addCustomMapper(Class, Function)
     * @since 1.0.1
     */
    public <T, R extends T> void addCustomStringMapper(Class<T> type, Function<String, R> mapper) {
        context.addCustomStringMapper(type, mapper);
    }

    /**
     * @param s string to print as line
     * @see Context#printLine(String)
     * @since 1.0.0
     */
    public void printLine(String s) {
        context.printLine(s);
    }

    /**
     * @param s string to print as error line
     * @see Context#printError(String)
     * @since 1.0.0
     */
    public void printError(String s) {
        context.printError(s);
    }

    @Override
    public String toString() {
        return "Shell";
    }

    private boolean handleInput(Input input) {
        Command command = commands.get(input.getCommand());
        if (command == null && fallback == null) {
            printError("command not found: " + input.getCommand());
            return false;
        }
        try {
            if (command != null) command.execute(input, context);
            else fallback.execute(input, context);
        } catch (MethodCallException e) {
            if (exceptionHandler != null) {
                try {
                    exceptionHandler.handleException(e, context);
                } catch (Exception eh) {
                    printError("error in exception handler: " + eh);
                }
            } else {
                String message = e.getMessage();
                if (e.getCause() instanceof InvocationTargetException) {
                    Throwable targetException = e.getCause().getCause();
                    message = targetException.getClass().getSimpleName();
                    if (targetException.getMessage() != null)
                        message += ": " + targetException.getMessage();
                }
                printError("error during execution: " + message);
            }
            return false;
        }
        return true;
    }

    private void printPrompt() {
        if (prompt.isEmpty()) return;
        context.getOutput().print(prompt);
        context.getOutput().flush();
    }

    private boolean isRunning() {
        return isThreadRunning(Thread.currentThread());
    }

    private static boolean isThreadRunning(Thread thread) {
        return thread.isAlive() && !thread.isInterrupted();
    }
}
