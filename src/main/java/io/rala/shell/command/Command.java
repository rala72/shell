package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;

public interface Command {
    default String getDocumentation() {
        return null;
    }

    default String getUsage() {
        return null;
    }

    void execute(Input input, Context context);
}
