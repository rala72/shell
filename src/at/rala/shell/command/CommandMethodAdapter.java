package at.rala.shell.command;

import at.rala.shell.Context;
import at.rala.shell.Input;
import at.rala.shell.annotation.CommandMethod;
import at.rala.shell.exception.MethodCallException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

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
    public void execute(Input input, Context context) {
        if (commandMethod.isParameterCountValid(input.getArguments().size())) {
            context.getError().println("error: expected argument count: " + commandMethod.getMinParameterCount());
            return;
        }
        try {
            Parameter[] parameters = commandMethod.getMethod().getParameters();
            List<Object> objects = new ArrayList<>();
            for (int i = 0; i < parameters.length; i++) {
                String argument = input.get(i).orElseThrow(() -> new MethodCallException("argument missing"));
                objects.add(mapParameter(parameters[i].getType(), argument));
                if (parameters[i].isVarArgs())
                    for (int j = i + 1; j < input.getArguments().size(); j++)
                        objects.add(mapParameter(parameters[i].getType(), argument));
            }
            commandMethod.getMethod().invoke(object, objects.toArray());
        } catch (IllegalAccessException e) {
            throw new MethodCallException("illegal access");
        } catch (InvocationTargetException e) {
            throw new MethodCallException(e);
        }
    }

    CommandMethod getCommandMethod() {
        return commandMethod;
    }

    private static Object mapParameter(Class<?> type, String value) {
        if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            return Boolean.parseBoolean(value);
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
        if (char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)) {
            return (value != null && value.length() == 1) ? value.charAt(0) : null;
        }
        return value;
    }
}
