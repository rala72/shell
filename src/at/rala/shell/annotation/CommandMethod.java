package at.rala.shell.annotation;

import java.lang.reflect.Method;

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
        return getMinParameterCount() <= i && i <= getMaxParameterCount();
    }

    public int getMinParameterCount() {
        return getMethod().getParameterCount();
    }

    public int getMaxParameterCount() {
        return getMethod().getParameters()[getMethod().getParameters().length - 1]
            .isVarArgs() ? Integer.MAX_VALUE : getMinParameterCount();
    }

    @Override
    public String toString() {
        return "CommandMethod{" +
            "command=" + command +
            ", method=" + method +
            '}';
    }
}
