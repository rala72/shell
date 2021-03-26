package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import io.rala.shell.exception.StopShellException;
import org.jetbrains.annotations.NotNull;

/**
 * command which throws {@link StopShellException} to stop shell
 *
 * @since 1.0.0
 */
public class ExitCommand implements Command {
    @Override
    public String getDocumentation() {
        return "stops shell";
    }

    @Override
    public String getUsage() {
        return "exit";
    }

    @Override
    public void execute(@NotNull Input input, @NotNull Context context) {
        if (input.hasArguments()) {
            context.printError("error: no arguments expected");
            return;
        }
        throw new StopShellException();
    }

    @Override
    @NotNull
    public String toString() {
        return "ExitCommand";
    }
}
