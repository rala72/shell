package at.rala.shell.command;

import at.rala.shell.Input;

import java.lang.reflect.InvocationTargetException;

public class CommandMethodAdapter implements Command {
    private final Object object;
    private final CommandMethod commandMethod;

    public CommandMethodAdapter(Object object, CommandMethod commandMethod) {
        this.object = object;
        this.commandMethod = commandMethod;
    }

    @Override
    public void execute(Input input) {
        try {
            commandMethod.getMethod().invoke(object, input.getArguments());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
