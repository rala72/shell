package at.rala.shell;

import at.rala.shell.annotation.CommandLoader;
import at.rala.shell.command.Command;
import at.rala.shell.exception.MethodCallException;
import at.rala.shell.exception.StopShellException;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class Shell implements Runnable {
    public static final String DEFAULT_PROMPT = "> ";

    private final BufferedReader input;
    private final Context context;
    private final Map<String, Command> commands = new TreeMap<>();
    private Command fallback;
    private String prompt = DEFAULT_PROMPT;
    private boolean isStopOnInvalidCommandEnabled = false;

    public Shell() {
        this(System.in, System.out, System.err);
    }

    public Shell(InputStream inputStream, OutputStream outputStream) {
        this(inputStream, outputStream, outputStream);
    }

    public Shell(InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        this.input = new BufferedReader(new InputStreamReader(inputStream));
        this.context = new Context(
            new PrintWriter(outputStream, true),
            new PrintWriter(errorStream, true),
            commands
        );
    }

    @Override
    public void run() {
        try {
            while (true) {
                prompt();
                String line = input.readLine();
                if (line == null) return;
                if (line.isBlank()) continue;
                boolean success = handleInput(Input.parse(line));
                if (isStopOnInvalidCommandEnabled() && !success) break;
            }
        } catch (StopShellException | IOException ignored) {
        }
    }

    public void setFallback(Command fallback) {
        this.fallback = fallback;
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
        commands.putAll(new CommandLoader(object).getCommandMethodMap());
    }

    public void register(DefaultCommand defaultCommand) {
        register(defaultCommand.getName(), defaultCommand.getCommand());
    }

    public void register(String name, Command command) {
        if (commands.containsKey(name))
            throw new IllegalStateException("command already present");
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
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void prompt() {
        if (prompt.isEmpty()) return;
        context.getOutput().print(prompt);
        context.getOutput().flush();
    }
}
