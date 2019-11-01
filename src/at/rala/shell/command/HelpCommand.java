package at.rala.shell.command;

import at.rala.shell.Context;
import at.rala.shell.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class HelpCommand implements Command {
    private static final String DEFAULT_DOCUMENTATION = "<no documentation found>";

    @Override
    public String getDocumentation() {
        return "prints help of all commands or provided ones";
    }

    @Override
    public void execute(Input input, Context context) {
        if (input.getArguments().isEmpty()) printAll(context);
        else printInput(context, input.getArguments());
    }

    private void printAll(Context context) {
        Set<String> commands = context.getCommands().keySet();
        int maxLength = commands.stream().mapToInt(String::length).max().orElse(0);
        String output = context.getCommands().entrySet().stream().map(stringCommandEntry -> {
            String command = stringCommandEntry.getKey();
            String documentation = getDocumentationOfCommand(stringCommandEntry.getValue());
            if (documentation == null) documentation = DEFAULT_DOCUMENTATION;
            return formatLine(command, documentation, maxLength);
        }).collect(Collectors.joining("\n"));
        context.getOutput().println(output);
    }

    private void printInput(Context context, List<String> arguments) {
        List<String> output = new ArrayList<>();
        int maxLength = arguments.stream().mapToInt(String::length).max().orElse(0);
        for (String argument : arguments) {
            Command command = context.getCommands().get(argument);
            if (command == null) {
                context.getError().println("command " + argument + " not found");
                return;
            }
            String documentation = getDocumentationOfCommand(command);
            if (documentation == null) documentation = DEFAULT_DOCUMENTATION;
            output.add(formatLine(argument, documentation, maxLength));
        }
        output.forEach(string -> context.getOutput().println(string));
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
