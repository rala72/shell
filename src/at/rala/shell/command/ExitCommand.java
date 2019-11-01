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
    public void execute(Input input, Context context) {
        throw new StopShellException();
    }
}
