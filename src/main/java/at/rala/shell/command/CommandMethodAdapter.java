package at.rala.shell.command;

import at.rala.shell.Context;
import at.rala.shell.Input;
import at.rala.shell.annotation.CommandMethod;
import at.rala.shell.exception.MethodCallException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

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
                if (parameter.isVarArgs()) {
                    Class<?> componentType = parameter.getType().getComponentType();
                    value = input.getArguments()
                        .subList(i, input.getArguments().size())
                        .stream()
                        .map(s -> mapParameter(componentType, s))
                        .map(componentType::cast)
                        .toArray(n -> (Object[]) Array.newInstance(componentType, n));
                } else {
                    String argument = input.get(i)
                        .orElseThrow(() -> new MethodCallException("argument missing"));
                    value = mapParameter(parameter.getType(), argument);
                }
                objects[i] = value;
            }
            commandMethod.getMethod().invoke(object, objects);
        } catch (IllegalAccessException e) {
            throw new MethodCallException("illegal access");
        } catch (InvocationTargetException e) {
            throw new MethodCallException(e);
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
}