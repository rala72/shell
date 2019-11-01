package at.rala.shell.command;

import at.rala.shell.Input;

public interface Command {
    void execute(Input input);
}
