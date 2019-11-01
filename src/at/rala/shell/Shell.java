package at.rala.shell;

import at.rala.shell.annotation.CommandLoader;
import at.rala.shell.command.Command;
import at.rala.shell.exception.MethodCallException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class Shell implements Runnable {
    private final BufferedReader input;
    private final Context context;
    private final Map<String, Command> commands = new HashMap<>();

    public Shell(Object object) {
        this(object, System.in, System.out, System.err);
    }

    public Shell(Object object, InputStream inputStream, OutputStream outputStream) {
        this(object, inputStream, outputStream, outputStream);
    }

    public Shell(Object object, InputStream inputStream, OutputStream outputStream, OutputStream errorStream) {
        this.input = new BufferedReader(new InputStreamReader(inputStream));
        this.context = new Context(
            new PrintWriter(outputStream, true),
            new PrintWriter(errorStream, true),
            commands
        );
        this.commands.putAll(new CommandLoader(object).getCommandMethodMap());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = input.readLine();
                if (line == null) return;
                if (line.isBlank()) continue;
                handleInput(Input.parse(line));
            }
        } catch (IOException ignored) {
        }
    }

    public void printLine(String s) {
        context.getOutput().println(s);
    }

    public void printError(String s) {
        context.getError().println(s);
    }

    private boolean handleInput(Input input) {
        Command command = commands.get(input.getCommand());
        if (command == null) {
            printError("command not found: " + input.getCommand());
            return false;
        }
        try {
            command.execute(input, context);
        } catch (MethodCallException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
