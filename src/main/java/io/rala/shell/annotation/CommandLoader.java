package io.rala.shell.annotation;

import io.rala.StringMapper;
import io.rala.shell.command.CommandMethodAdapter;
import io.rala.shell.exception.CommandAlreadyPresentException;
import io.rala.shell.exception.IllegalParameterException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * loads all {@link Command}s and {@link Optional}s and validates them
 *
 * @since 1.0.0
 */
public class CommandLoader {
    private final Map<String, io.rala.shell.command.Command> commandMethodMap = new HashMap<>();

    /**
     * @param object       object to load all {@link Command}s from
     * @param stringMapper stringMapper for argument to object mapping
     * @throws CommandAlreadyPresentException if multiple methods use the same command
     * @throws IllegalParameterException      if any parameter is invalid
     * @see IllegalParameterException
     * @since 1.0.0
     */
    public CommandLoader(@NotNull Object object, @NotNull StringMapper stringMapper) {
        if (!Modifier.isPublic(object.getClass().getModifiers()))
            throw new IllegalArgumentException("object has to be public");
        List<Method> methodList = List.of(object.getClass().getMethods())
            .stream()
            .filter(method -> method.isAnnotationPresent(Command.class))
            .collect(Collectors.toList());
        if (methodList.isEmpty())
            throw new IllegalArgumentException("object has no visible commands");
        methodList.forEach(method -> {
            CommandMethod commandMethod = new CommandMethod(method.getAnnotation(Command.class), method);
            validateCommandMethod(commandMethod, stringMapper);
            getCommandMethodMap().put(
                commandMethod.getName(),
                new CommandMethodAdapter(object, commandMethod)
            );
        });
    }

    /**
     * @return loaded {@link io.rala.shell.command.Command} map
     * @since 1.0.0
     */
    @NotNull
    public Map<String, io.rala.shell.command.Command> getCommandMethodMap() {
        return commandMethodMap;
    }

    @Override
    @NotNull
    public String toString() {
        return String.join(",", getCommandMethodMap().keySet());
    }

    private void validateCommandMethod(
        @NotNull CommandMethod commandMethod, @NotNull StringMapper stringMapper
    ) {
        if (getCommandMethodMap().containsKey(commandMethod.getName()))
            throw new CommandAlreadyPresentException(commandMethod.getName());
        CommandParameter[] parameters = commandMethod.getParameters();
        long inputCount = Stream.of(parameters)
            .filter(CommandParameter::isInput)
            .count();
        if (0 < inputCount && parameters.length != 1)
            throw IllegalParameterException.createNewIfInputNoOther(commandMethod.getName());
        long dynamicParameterCount = Stream.of(parameters)
            .filter(CommandParameter::isDynamic)
            .count();
        if (1 < dynamicParameterCount)
            throw IllegalParameterException.createNewOnlyOneDynamicInstance(commandMethod.getName());
        if (dynamicParameterCount == 1 && !commandMethod.isLastParameterDynamic())
            throw IllegalParameterException.createNewOnlyLastDynamicInstance(commandMethod.getName());
        if (Arrays.stream(parameters).anyMatch(CommandParameter::isOptional)) {
            int previousOptionalParameter = 0;
            for (CommandParameter parameter : parameters) {
                boolean parameterOptional = parameter.isOptional();
                if (!parameterOptional && 0 < previousOptionalParameter)
                    throw IllegalParameterException
                        .createNewOnlyLastParametersCanBeAbsent(commandMethod.getName());
                if (parameterOptional) previousOptionalParameter++;
            }
            for (CommandParameter parameter : parameters)
                validateOptionalDefaultValue(parameter, stringMapper);
        }
    }

    private static void validateOptionalDefaultValue(
        @NotNull CommandParameter parameter, @NotNull StringMapper stringMapper
    ) {
        Optional annotation = parameter.getOptionalAnnotation();
        if (annotation == null) return;
        String value = annotation.value();
        if (value.isEmpty()) return;
        try {
            stringMapper.map(value, parameter.getType());
        } catch (IllegalArgumentException e) {
            throw IllegalParameterException.createNewOptionalDefaultValueIsInvalid(parameter.getName());
        }
    }
}
