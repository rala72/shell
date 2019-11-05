package io.rala.shell.annotation;

import io.rala.shell.command.CommandMethodAdapter;
import io.rala.shell.exception.CommandAlreadyPresentException;
import io.rala.shell.exception.IllegalParameterException;

import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CommandLoader {
    private final Map<String, io.rala.shell.command.Command> commandMethodMap = new HashMap<>();

    public CommandLoader(Object object) {
        if (!Modifier.isPublic(object.getClass().getModifiers()))
            throw new IllegalArgumentException("object has to be public");
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
        if (getCommandMethodMap().containsKey(commandMethod.getName()))
            throw new CommandAlreadyPresentException(commandMethod.getName());
        Parameter[] parameters = commandMethod.getMethod().getParameters();
        long dynamicParameterCount = Stream.of(parameters)
            .filter(CommandMethod::isParameterDynamic)
            .count();
        if (1 < dynamicParameterCount)
            throw IllegalParameterException.createNewOnlyOneDynamicInstance(commandMethod.getName());
        if (dynamicParameterCount == 1 && !commandMethod.isLastParameterDynamic())
            throw IllegalParameterException.createNewOnlyLastDynamicInstance(commandMethod.getName());
    }
}
