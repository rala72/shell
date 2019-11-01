package at.rala.shell.command;

import at.rala.shell.Context;
import at.rala.shell.Input;
import at.rala.shell.exception.StopShellException;

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
