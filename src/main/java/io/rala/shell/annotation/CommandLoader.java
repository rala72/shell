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
        Parameter[] parameters = commandMethod.getMethod().getParameters();
        long arrayParameterCount = Stream.of(parameters)
            .map(Parameter::getType)
            .filter(Class::isArray)
            .count();
        if (1 < arrayParameterCount)
            throw IllegalParameterException.createNewOnlyOneArrayInstance(commandMethod.getName());
        if (arrayParameterCount == 1 && !commandMethod.isLastParameterDynamic())
            throw IllegalParameterException.createNewOnlyLastArrayOrVararg(commandMethod.getName());
        if (getCommandMethodMap().containsKey(commandMethod.getName()))
            throw new CommandAlreadyPresentException(commandMethod.getName());
    }
}
