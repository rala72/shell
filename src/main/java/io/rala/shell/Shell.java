package io.rala.shell;

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

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class Shell implements Runnable {
    public static final String DEFAULT_PROMPT = "> ";

    private final ReaderQueue readerQueue;
    private final Context context;
    private final Map<String, Command> commands = new TreeMap<>();
    private Command fallback;
    private ExceptionHandler exceptionHandler;
    private String prompt = DEFAULT_PROMPT;
    private boolean isStopOnInvalidCommandEnabled = false;

    public Shell() {
        this(System.in, System.out, System.err);
    }

    public Shell(InputStream inputStream, OutputStream outputStream) {
        this(inputStream, outputStream, outputStream);
    }

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
        Thread thread = new Thread();
        try {
            thread = new Thread(readerQueue);
            thread.start();
            out:
            while (isRunning()) {
                printPrompt();
                String line = readerQueue.peek();
                while (line == null || line.isBlank()) {
                    if (!isRunning() || !isThreadRunning(thread)) break out;
                    line = readerQueue.peek();
                }
                if (!isRunning() || !isThreadRunning(thread)) break;
                line = readerQueue.take();
                boolean success = handleInput(Input.parse(line));
                if (isStopOnInvalidCommandEnabled() && !success) break;
            }
        } catch (StopShellException ignored) {
        }
        thread.interrupt();
    }

    public void setFallback(Command fallback) {
        this.fallback = fallback;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void setPrompt(String prompt) {
        if (prompt == null) prompt = DEFAULT_PROMPT;
        this.prompt = prompt;
    }

    public boolean isStopOnInvalidCommandEnabled() {
        return isStopOnInvalidCommandEnabled;
    }

    public void setStopOnInvalidCommandEnabled(boolean stopOnInvalidCommandEnabled) {
        isStopOnInvalidCommandEnabled = stopOnInvalidCommandEnabled;
    }

    public void register(Object object) {
        new CommandLoader(object).getCommandMethodMap()
            .forEach(this::register);
    }

    public void register(DefaultCommand command) {
        register(command.getName(), command.getCommand());
    }

    public void register(DefaultCommand command, DefaultCommand... furtherCommands) {
        if (command != null) register(command);
        register(furtherCommands);
    }

    public void register(DefaultCommand[] commands) {
        for (DefaultCommand command : commands)
            register(command);
    }

    public void register(String name, Command command) {
        if (commands.containsKey(name))
            throw new CommandAlreadyPresentException(name);
        commands.put(name, command);
    }

    public void printLine(String s) {
        context.printLine(s);
    }

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
                    InvocationTargetException cause = (InvocationTargetException) e.getCause();
                    Throwable targetException = cause.getTargetException();
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
