package at.rala.shell.command;

import at.rala.shell.Context;
import at.rala.shell.Input;

public interface Command {
    void execute(Input input, Context context);
}
