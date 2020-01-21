package io.rala.shell;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@link Shell} input holder
 */
public class Input {
    private final String command;
    private final List<String> arguments;

    /**
     * @param command   command name
     * @param arguments optional arguments
     */
    public Input(String command, String... arguments) {
        this(command, Arrays.asList(arguments));
    }

    /**
     * @param command   command name
     * @param arguments argument list
     */
    public Input(String command, List<String> arguments) {
        this.command = command;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    /**
     * @return command name
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return argument list
     */
    public List<String> getArguments() {
        return arguments;
    }

    /**
     * @return {@code true} if arguments are present
     */
    public boolean hasArguments() {
        return !getArguments().isEmpty();
    }

    /**
     * @param i index of parameter
     * @return optional which has argument or is empty
     */
    public Optional<String> get(int i) {
        return 0 <= i && i < getArguments().size() ?
            Optional.ofNullable(getArguments().get(i)) : Optional.empty();
    }

    /**
     * @param i index of parameter
     * @return parameter or null
     */
    public String getOrNull(int i) {
        return get(i).orElse(null);
    }

    @Override
    public String toString() {
        return "Input{" +
            "command='" + command + '\'' +
            ", arguments=" + arguments +
            '}';
    }

    /**
     * @param line line to parse
     * @return new Input instance parsed by line without filtering blanks
     * @see #parse(String, boolean)
     */
    public static Input parse(String line) {
        return parse(line, false);
    }

    /**
     * @param line        line to parse
     * @param filterBlank filter blank strings
     * @return new Input instance parsed by line
     */
    public static Input parse(String line, boolean filterBlank) {
        List<String> parts = List.of(line.split(" ")).stream()
            .filter(string -> !filterBlank || !string.isEmpty())
            .collect(Collectors.toList());
        return new Input(
            1 <= parts.size() ? parts.get(0) : null,
            1 <= parts.size() ? parts.subList(1, parts.size()) : Collections.emptyList()
        );
    }
}
