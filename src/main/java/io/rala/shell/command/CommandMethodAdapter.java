package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import io.rala.shell.annotation.CommandMethod;
import io.rala.shell.annotation.CommandParameter;
import io.rala.shell.exception.MethodCallException;
import io.rala.shell.utils.StringMapper;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Stream;

public class CommandMethodAdapter implements Command {
    private final Object object;
    private final CommandMethod commandMethod;

    public CommandMethodAdapter(Object object, CommandMethod commandMethod) {
        this.object = object;
        this.commandMethod = commandMethod;
    }

    @Override
    public String getDocumentation() {
        return getCommandMethod().getCommand().documentation();
    }

    @Override
    public String getUsage() {
        return getCommandMethod().getCommand().usage();
    }

    @Override
    public void execute(Input input, Context context) {
        if (!commandMethod.isParameterCountValid(input.getArguments().size())) {
            context.printError("error: expected argument count: " + commandMethod.getMinParameterCount());
            if (!getUsage().isEmpty()) context.printError(getUsage());
            return;
        }
        try {
            CommandParameter[] parameters = commandMethod.getParameters();
            Object[] objects = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object value;
                CommandParameter parameter = parameters[i];
                if (parameter.isInput()) {
                    value = input;
                } else if (i == parameters.length - 1 && commandMethod.isLastParameterDynamic()) {
                    Class<?> componentType =
                        parameter.isArray() ?
                            parameter.getType().getComponentType() :
                            getFirstGenericClass(commandMethod.getMethod());
                    Object[] array = input.getArguments()
                        .subList(i, input.getArguments().size())
                        .stream()
                        .map(s -> new StringMapper(s).map(componentType))
                        .map(componentType::cast)
                        .toArray(n -> (Object[]) Array.newInstance(componentType, n));
                    value = parameter.isArray() ? array : List.of(array);
                } else {
                    String argument = input.get(i)
                        .orElseThrow(() -> new MethodCallException("argument missing"));
                    value = new StringMapper(argument).map(parameter.getType());
                }
                objects[i] = value;
            }
            commandMethod.getMethod().invoke(object, objects);
        } catch (IllegalArgumentException | NullPointerException | InvocationTargetException e) {
            throw new MethodCallException(e);
        } catch (IllegalAccessException e) {
            throw new MethodCallException("illegal access");
        }
    }

    @Override
    public String toString() {
        return "CommandMethodAdapter{" +
            "object=" + object +
            ", commandMethod=" + commandMethod +
            '}';
    }

    CommandMethod getCommandMethod() {
        return commandMethod;
    }

    private static Class<?> getFirstGenericClass(Method method) {
        // http://tutorials.jenkov.com/java-reflection/generics.html#parametertypes
        return Stream.of(method.getGenericParameterTypes())
            .filter(type -> type instanceof ParameterizedType)
            .map(type -> (ParameterizedType) type)
            .map(ParameterizedType::getActualTypeArguments)
            .map(types -> 0 < types.length ? (Class) types[0] : String.class)
            .findFirst().orElse(String.class);
    }
}
