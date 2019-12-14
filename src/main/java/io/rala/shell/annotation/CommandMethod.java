package io.rala.shell.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class CommandMethod {
    private final Command command;
    private final Method method;

    public CommandMethod(Command command, Method method) {
        this.command = command;
        this.method = method;
    }

    public String getName() {
        return !command.value().isEmpty() ? command.value() : method.getName();
    }

    public Command getCommand() {
        return command;
    }

    public Method getMethod() {
        return method;
    }

    public int getMinParameterCount() {
        return Math.toIntExact(Arrays.stream(getParameters())
            .filter(commandParameter -> !commandParameter.isOptional())
            .filter(commandParameter -> !commandParameter.isDynamic())
            .filter(commandParameter -> !commandParameter.isContext())
            .count()
        );
    }

    public int getMaxParameterCount() {
        return isLastParameterDynamic() ? Integer.MAX_VALUE :
            Math.toIntExact(
                Arrays.stream(getParameters())
                    .filter(commandParameter -> !commandParameter.isContext())
                    .count()
            ) + 1;
    }

    public boolean isParameterCountValid(int i) {
        return getMinParameterCount() <= i && i < getMaxParameterCount();
    }

    public boolean isLastParameterDynamic() {
        if (getMethod().getParameterCount() == 0) return false;
        Parameter lastParameter = getMethod().getParameters()[getMethod().getParameters().length - 1];
        return new CommandParameter(lastParameter).isDynamic();
    }

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
