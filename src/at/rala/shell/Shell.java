package at.rala.shell;

import at.rala.shell.annotation.Command;
import at.rala.shell.command.CommandMethod;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Shell implements Runnable {
    private final Object object;
    private final BufferedReader input;
    private final PrintWriter output;
    private final Map<String, CommandMethod> commandMethodMap = new HashMap<>();

    public Shell(Object object) {
        this(object, System.in, System.out);
    }

    public Shell(Object object, InputStream inputStream, OutputStream outputStream) {
        this.object = object;
        this.input = new BufferedReader(new InputStreamReader(inputStream));
        this.output = new PrintWriter(outputStream, true);
        register();
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
        String command = input.getCommand();
        CommandMethod commandMethod = commandMethodMap.get(command);
        if (commandMethod == null) {
            printLine("command not found: " + command);
            return false;
        }
        return true;
    }

    private void register() {
        List.of(object.getClass().getMethods())
            .stream()
            .filter(method -> method.isAnnotationPresent(Command.class))
            .forEach(method -> {
                CommandMethod commandMethod = new CommandMethod(method.getAnnotation(Command.class), method);
                if (commandMethodMap.containsKey(commandMethod.getName()))
                    throw new IllegalStateException(commandMethod.getName() + " is defined more than once");
                    else
                    commandMethodMap.put(commandMethod.getName(), commandMethod);
                }
            );
    }
}
