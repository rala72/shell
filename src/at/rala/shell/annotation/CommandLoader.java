package at.rala.shell.annotation;

import at.rala.shell.command.CommandMethodAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandLoader {
    private final Map<String, at.rala.shell.command.Command> commandMethodMap = new HashMap<>();

    public CommandLoader(Object object) {
        List.of(object.getClass().getMethods())
            .stream()
            .filter(method -> method.isAnnotationPresent(Command.class))
            .forEach(method -> {
                CommandMethod commandMethod = new CommandMethod(method.getAnnotation(Command.class), method);
                if (getCommandMethodMap().containsKey(commandMethod.getName()))
                    throw new IllegalStateException(commandMethod.getName() + " is defined more than once");
                else
                    getCommandMethodMap().put(
                        commandMethod.getName(),
                        new CommandMethodAdapter(object, commandMethod)
                    );
            });
    }

    public Map<String, at.rala.shell.command.Command> getCommandMethodMap() {
        return commandMethodMap;
    }
}
