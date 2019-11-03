package io.rala.shell;

import io.rala.shell.command.Command;
import io.rala.shell.command.ExitCommand;
import io.rala.shell.command.HelpCommand;

@SuppressWarnings("unused")
public enum DefaultCommand {
    EXIT("exit", new ExitCommand()),
    HELP("help", new HelpCommand());

    private final String name;
    private final Command command;

    DefaultCommand(String name, Command command) {
        this.name = name;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return name;
    }
}
