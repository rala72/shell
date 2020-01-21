package io.rala.shell.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * connection of {@link Command} and {@link Method}
 */
public class CommandMethod {
    private final Command command;
    private final Method method;

    /**
     * @param command command of connection
     * @param method  method of connection
     */
    public CommandMethod(Command command, Method method) {
        this.command = command;
        this.method = method;
    }

    /**
     * @return name of command
     */
    public String getName() {
        return !command.value().isEmpty() ? command.value() : method.getName();
    }

    /**
     * @return command of connection
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @return method of connection
     */
    public Method getMethod() {
        return method;
    }

    /**
     * @return min count of parameters
     */
    public int getMinParameterCount() {
        return Math.toIntExact(Arrays.stream(getParameters())
            .filter(commandParameter -> !commandParameter.isOptional())
            .filter(commandParameter -> !commandParameter.isDynamic())
            .filter(commandParameter -> !commandParameter.isContext())
            .count()
        );
    }

    /**
     * @return max count of parameters + 1
     */
    public int getMaxParameterCount() {
        return isLastParameterDynamic() ? Integer.MAX_VALUE :
            Math.toIntExact(
                Arrays.stream(getParameters())
                    .filter(commandParameter -> !commandParameter.isContext())
                    .count()
            ) + 1;
    }

    /**
     * @param i amount of parameters
     * @return {@code true} if amount is in boundaries
     */
    public boolean isParameterCountValid(int i) {
        return getMinParameterCount() <= i && i < getMaxParameterCount();
    }

    /**
     * @return {@code true} if last parameter is dynamic
     */
    public boolean isLastParameterDynamic() {
        if (getMethod().getParameterCount() == 0) return false;
        Parameter lastParameter = getMethod().getParameters()[getMethod().getParameters().length - 1];
        return new CommandParameter(lastParameter).isDynamic();
    }

    /**
     * @return command parameters
     */
    public CommandParameter[] getParameters() {
        return Arrays.stream(getMethod().getParameters())
            .map(CommandParameter::new)
            .toArray(CommandParameter[]::new);
    }

    @Override
    public String toString() {
        return "CommandMethod{" +
            "command=" + convertCommandToString(command) +
            ", method=" + method.getName() +
            '}';
    }

    private static String convertCommandToString(Command command) {
        return command == null ? null :
            "Command(value=\"" + command.value() + "\"" +
                ", documentation=\"" + command.documentation() + "\"" +
                ")";
    }
}
