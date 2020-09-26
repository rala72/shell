package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import io.rala.shell.exception.MethodCallException;

/**
 * declaration of command implementation
 *
 * @since 1.0.0
 */
public interface Command {
    /**
     * @return optional documentation
     * @since 1.0.0
     */
    default String getDocumentation() {
        return null;
    }

    /**
     * @return optional usage
     * @since 1.0.0
     */
    default String getUsage() {
        return null;
    }

    /**
     * called if command is executed
     *
     * @param input   input of command
     * @param context context of shell
     * @throws MethodCallException if any known exception is thrown
     * @since 1.0.0
     */
    void execute(Input input, Context context);
}
