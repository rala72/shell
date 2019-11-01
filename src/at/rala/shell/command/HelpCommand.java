package at.rala.shell.command;

import at.rala.shell.Context;
import at.rala.shell.Input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
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
    public void execute(Input input, Context context) {
        Collection<String> commands = input.hasArguments() ?
            input.getArguments() : context.getCommands().keySet();
        printCommands(context, commands);
    }

    @Override
    public String toString() {
        return "HelpCommand";
    }

    private void printCommands(Context context, Collection<String> arguments) {
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
        context.printLine(String.join("\n", output));
    }

    private String formatLine(String command, String documentation, int maxLength) {
        return String.format("%-" + maxLength + "s", command) + " \t" + documentation;
    }

    private String getDocumentationOfCommand(Command command) {
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
