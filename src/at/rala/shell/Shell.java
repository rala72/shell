package at.rala.shell;

import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Shell implements Runnable {
    private final Object object;
    private final BufferedReader input;
    private final PrintWriter output;
    private final Map<String, CommandMethodHolder> commandMethodMap = new HashMap<>();

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
        CommandMethodHolder commandMethodHolder = commandMethodMap.get(command);
        if (commandMethodHolder == null) {
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
                    CommandMethodHolder commandMethodHolder = new CommandMethodHolder(method.getAnnotation(Command.class), method);
                    if (commandMethodMap.containsKey(commandMethodHolder.getName()))
                        throw new IllegalStateException(commandMethodHolder.getName() + " is defined more than once");
                    else
                        commandMethodMap.put(commandMethodHolder.getName(), commandMethodHolder);
                }
            );
    }

    private static class CommandMethodHolder {
        private final Command command;
        private final Method method;

        private CommandMethodHolder(Command command, Method method) {
            this.command = command;
            this.method = method;
        }

        private String getName() {
            return !command.value().isEmpty() ? command.value() : method.getName();
        }

        private void validateArguments(List<String> arguments) {

        }
    }
}
