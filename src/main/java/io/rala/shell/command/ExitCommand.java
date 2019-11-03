package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import io.rala.shell.exception.StopShellException;

@SuppressWarnings("unused")
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
    public void execute(Input input, Context context) {
        if (input.hasArguments()) {
            context.printError("error: no arguments expected");
            return;
        }
        throw new StopShellException();
    }

    @Override
    public String toString() {
        return "ExitCommand";
    }
}
