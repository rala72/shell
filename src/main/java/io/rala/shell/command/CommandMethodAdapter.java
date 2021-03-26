package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import io.rala.shell.annotation.CommandMethod;
import io.rala.shell.annotation.CommandParameter;
import io.rala.shell.annotation.Optional;
import io.rala.shell.exception.MethodCallException;
import io.rala.shell.utils.Default;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Stream;

/**
 * adapter for {@link CommandMethod} to implement {@link Command}
 *
 * @since 1.0.0
 */
public class CommandMethodAdapter implements Command {
    private static final char INFINITY = '∞';
    private final Object object;
    private final CommandMethod commandMethod;

    /**
     * @param object        object which has the method
     * @param commandMethod command method of object
     * @since 1.0.0
     */
    public CommandMethodAdapter(Object object, CommandMethod commandMethod) {
        this.object = object;
        this.commandMethod = commandMethod;
    }

    @Override
    @Nullable
    public String getDocumentation() {
        return getCommandMethod().getCommand().documentation();
    }

    @Override
    @Nullable
    public String getUsage() {
        return getCommandMethod().getCommand().usage();
    }

    @Override
    public void execute(@NotNull Input input, @NotNull Context context) {
        if (!validateParameterCount(input, context)) return;
        try {
            CommandParameter[] parameters = getCommandMethod().getParameters();
            Object[] objects = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object value;
                CommandParameter parameter = parameters[i];
                if (parameter.isInput()) {
                    value = input;
                } else if (parameter.isContext()) {
                    value = context;
                } else if (i == parameters.length - 1 && getCommandMethod().isLastParameterDynamic()) {
                    value = handleDynamicParameter(input, context, parameter, i);
                } else {
                    value = handleParameter(input, context, parameter, i);
                }
                objects[i] = value;
            }
            Object result = getCommandMethod().getMethod().invoke(object, objects);
            if (result != null)
                context.printLine(String.valueOf(result));
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
            ", commandMethod=" + getCommandMethod() +
            '}';
    }

    @NotNull
    CommandMethod getCommandMethod() {
        return commandMethod;
    }

    private boolean validateParameterCount(@NotNull Input input, @NotNull Context context) {
        if (getCommandMethod().isParameterCountValid(input.getArguments().size()))
            return true;
        String expectedArgumentCount = String.valueOf(getCommandMethod().getMinParameterCount());
        if (getCommandMethod().getMinParameterCount() != getCommandMethod().getMaxParameterCount() - 1)
            expectedArgumentCount += "-" +
                (getCommandMethod().getMaxParameterCount() == Integer.MAX_VALUE ?
                    INFINITY : getCommandMethod().getMaxParameterCount() - 1);
        context.printError("error: expected argument count: " + expectedArgumentCount);
        if (getUsage() != null && !getUsage().isEmpty()) context.printError(getUsage());
        return false;
    }

    @NotNull
    private Object handleDynamicParameter(
        @NotNull Input input, @NotNull Context context,
        @NotNull CommandParameter parameter, int i
    ) {
        Class<?> componentType =
            parameter.isArray() ?
                parameter.getType().getComponentType() :
                getFirstGenericClass(getCommandMethod().getMethod());
        Object[] array = input.getArguments()
            .subList(i, input.getArguments().size())
            .stream()
            .map(s -> context.getStringMapper().map(s, componentType))
            .map(componentType::cast)
            .toArray(n -> (Object[]) Array.newInstance(componentType, n));
        return parameter.isArray() ? array : List.of(array);
    }

    @Nullable
    private Object handleParameter(
        @NotNull Input input, @NotNull Context context,
        @NotNull CommandParameter parameter, int i
    ) {
        Optional optionalAnnotation = parameter.getOptionalAnnotation();
        String argument = input.getOrNull(i);
        if (argument == null && optionalAnnotation != null)
            argument = optionalAnnotation.value().isEmpty() ?
                null : optionalAnnotation.value();
        return argument != null || optionalAnnotation == null ?
            context.getStringMapper().map(argument, parameter.getType()) :
            Default.of(parameter.getType());
    }

    @NotNull
    private static Class<?> getFirstGenericClass(Method method) {
        // http://tutorials.jenkov.com/java-reflection/generics.html#parametertypes
        return Stream.of(method.getGenericParameterTypes())
            .filter(type -> type instanceof ParameterizedType)
            .map(type -> (ParameterizedType) type)
            .map(ParameterizedType::getActualTypeArguments)
            .map(types -> 0 < types.length ? (Class<?>) types[0] : String.class)
            .findFirst().orElse(String.class);
    }
}
