package io.rala.shell.annotation;

import io.rala.shell.command.CommandMethodAdapter;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommandLoader {
    private final Map<String, io.rala.shell.command.Command> commandMethodMap = new HashMap<>();

    public CommandLoader(Object object) {
        List.of(object.getClass().getMethods())
            .stream()
            .filter(method -> method.isAnnotationPresent(Command.class))
            .forEach(method -> {
                CommandMethod commandMethod = new CommandMethod(method.getAnnotation(Command.class), method);
                validateCommandMethod(commandMethod);
                getCommandMethodMap().put(
                    commandMethod.getName(),
                    new CommandMethodAdapter(object, commandMethod)
                );
            });
    }

    public Map<String, io.rala.shell.command.Command> getCommandMethodMap() {
        return commandMethodMap;
    }

    @Override
    public String toString() {
        return String.join(",", commandMethodMap.keySet());
    }

    private void validateCommandMethod(CommandMethod commandMethod) {
        Parameter[] parameters = commandMethod.getMethod().getParameters();
        long arrayParameterCount = Stream.of(parameters)
            .map(Parameter::getType)
            .filter(Class::isArray)
            .count();
        if (1 < arrayParameterCount)
            throw new IllegalArgumentException(commandMethod.getName() + ": may only have one array parameter");
        if (arrayParameterCount == 1 && !commandMethod.isLastParameterDynamic())
            throw new IllegalArgumentException(commandMethod.getName() + ": only last parameter may be an array or vararg");
        if (getCommandMethodMap().containsKey(commandMethod.getName()))
            throw new IllegalStateException(commandMethod.getName() + " is defined more than once");
    }
}
