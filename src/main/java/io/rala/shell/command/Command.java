package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;

/**
 * declaration of command implementation
 */
public interface Command {
    /**
     * @return optional documentation
     */
    default String getDocumentation() {
        return null;
    }

    /**
     * @return optional usage
     */
    default String getUsage() {
        return null;
    }

    /**
     * called if command is executed
     *
     * @param input   input of command
     * @param context context of shell
     */
    void execute(Input input, Context context);
}
