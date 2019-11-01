package at.rala.shell;

import at.rala.shell.command.Command;
import at.rala.shell.command.ExitCommand;
import at.rala.shell.command.HelpCommand;

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
