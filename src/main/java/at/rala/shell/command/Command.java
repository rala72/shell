package at.rala.shell.command;

import at.rala.shell.Context;
import at.rala.shell.Input;

public interface Command {
    default String getDocumentation() {
        return null;
    }

    default String getUsage() {
        return null;
    }

    void execute(Input input, Context context);
}
