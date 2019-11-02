package at.rala.shell.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@SuppressWarnings({"unused", "WeakerAccess"})
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

    public boolean isParameterCountValid(int i) {
        return getMinParameterCount() <= i && i < getMaxParameterCount();
    }

    public int getMinParameterCount() {
        return 0 < getMethod().getParameterCount() && isLastParameterDynamic() ?
            getMethod().getParameterCount() - 1 : getMethod().getParameterCount();
    }

    public int getMaxParameterCount() {
        return 0 == getMethod().getParameterCount() ? 0 :
            isLastParameterDynamic() ? Integer.MAX_VALUE : getMinParameterCount() + 1;
    }

    @Override
    public String toString() {
        return "CommandMethod{" +
            "command=" + convertCommandToString(command) +
            ", method=" + method.getName() +
            '}';
    }

    private boolean isLastParameterDynamic() {
        if (getMethod().getParameterCount() == 0) return false;
        Parameter lastParameter = getMethod().getParameters()[getMethod().getParameters().length - 1];
        return isDynamicParameter(lastParameter);
    }

    private static boolean isDynamicParameter(Parameter parameter) {
        return parameter.isVarArgs() || parameter.getType().isArray();
    }

    private static String convertCommandToString(Command command) {
        return command == null ? null :
            "Command(value=\"" + command.value() + "\"" +
                ", documentation=\"" + command.documentation() + "\"" +
                ")";
    }
}
