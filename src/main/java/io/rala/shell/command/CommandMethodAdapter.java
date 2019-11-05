package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import io.rala.shell.annotation.CommandMethod;
import io.rala.shell.exception.MethodCallException;

import java.lang.reflect.*;
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
            Parameter[] parameters = commandMethod.getMethod().getParameters();
            Object[] objects = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object value;
                Parameter parameter = parameters[i];
                if (i == parameters.length - 1 && commandMethod.isLastParameterDynamic()) {
                    Class<?> componentType =
                        CommandMethod.isParameterArray(parameter) ?
                            parameter.getType().getComponentType() :
                            getFirstGenericClass(commandMethod.getMethod());
                    Object[] array = input.getArguments()
                        .subList(i, input.getArguments().size())
                        .stream()
                        .map(s -> mapParameter(componentType, s))
                        .map(componentType::cast)
                        .toArray(n -> (Object[]) Array.newInstance(componentType, n));
                    value = CommandMethod.isParameterArray(parameter) ? array : List.of(array);
                } else {
                    String argument = input.get(i)
                        .orElseThrow(() -> new MethodCallException("argument missing"));
                    value = mapParameter(parameter.getType(), argument);
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

    private static Object mapParameter(Class<?> type, String value) {
        if (!type.isPrimitive() && value.equals("null")) return null;
        if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            return Boolean.parseBoolean(value);
        }
        if (byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
            return Byte.parseByte(value);
        }
        if (char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)) {
            return value.length() == 1 ? value.charAt(0) : null;
        }
        if (short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
            return Short.parseShort(value);
        }
        if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
            return Integer.parseInt(value);
        }
        if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            return Long.parseLong(value);
        }
        if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
            return Float.parseFloat(value);
        }
        if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
            return Double.parseDouble(value);
        }
        return value;
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
