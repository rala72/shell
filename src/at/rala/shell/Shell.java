package at.rala.shell;

import at.rala.shell.annotation.CommandLoader;
import at.rala.shell.command.Command;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class Shell implements Runnable {
    private final BufferedReader input;
    private final PrintWriter output;
    private final Map<String, Command> commands = new HashMap<>();

    public Shell(Object object) {
        this(object, System.in, System.out);
    }

    public Shell(Object object, InputStream inputStream, OutputStream outputStream) {
        this.input = new BufferedReader(new InputStreamReader(inputStream));
        this.output = new PrintWriter(outputStream, true);
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
        output.println(s);
    }

    private boolean handleInput(Input input) {
        Command command = commands.get(input.getCommand());
        if (command == null) {
            printLine("command not found: " + input.getCommand());
            return false;
        }
        command.execute(input);
        return true;
    }
}
