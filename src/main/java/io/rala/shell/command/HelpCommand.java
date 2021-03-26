package io.rala.shell.command;

import io.rala.shell.Context;
import io.rala.shell.Input;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * command which prints out the help of all commands or requested ones
 *
 * @since 1.0.0
 */
public class HelpCommand implements Command {
    private static final String DEFAULT_DOCUMENTATION = "<no documentation found>";

    @Override
    public String getDocumentation() {
        return "prints help of all commands or provided ones";
    }

    @Override
    public String getUsage() {
        return "help [command [command ...]]";
    }

    @Override
    public void execute(@NotNull Input input, @NotNull Context context) {
        Collection<String> commands = input.hasArguments() ?
            input.getArguments() : context.getCommands().keySet();
        printCommands(context, commands);
    }

    @Override
    @NotNull
    public String toString() {
        return "HelpCommand";
    }

    private void printCommands(@NotNull Context context, @NotNull Collection<String> arguments) {
        List<String> output = new ArrayList<>();
        int maxLength = arguments.stream().mapToInt(String::length).max().orElse(0);
        for (String argument : arguments) {
            Command command = context.getCommands().get(argument);
            if (command == null) {
                context.printError("error: command " + argument + " not found");
                return;
            }
            String documentation = getDocumentationOfCommand(command);
            if (documentation == null) documentation = DEFAULT_DOCUMENTATION;
            output.add(formatLine(argument, documentation, maxLength));
        }
        context.printLine(output.isEmpty() ? "no commands found" : String.join("\n", output));
    }

    @NotNull
    private String formatLine(@NotNull String command, @NotNull String documentation, int maxLength) {
        return String.format("%-" + maxLength + "s", command) + " \t" + documentation;
    }

    @Nullable
    private String getDocumentationOfCommand(@NotNull Command command) {
        if (command.getDocumentation() != null && !command.getDocumentation().isEmpty())
            return command.getDocumentation();
        if (command instanceof CommandMethodAdapter) {
            CommandMethodAdapter methodAdapter = (CommandMethodAdapter) command;
            String documentation = methodAdapter.getCommandMethod().getCommand().documentation();
            return documentation.isEmpty() ? null : documentation;
        }
        return null;
    }
}
