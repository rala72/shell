package at.rala.shell;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Input {
    private final String command;
    private final List<String> arguments;

    public Input(String command, String... arguments) {
        this(command, Arrays.asList(arguments));
    }

    public Input(String command, List<String> arguments) {
        this.command = command;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public Optional<String> get(int i) {
        return 0 <= i && i < getArguments().size() ?
            Optional.ofNullable(getArguments().get(i)) : Optional.empty();
    }

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

    public static Input parse(String line) {
        return parse(line, false);
    }

    public static Input parse(String line, boolean filterBlank) {
        List<String> parts = List.of(line.split(" ")).stream()
            .filter(string -> !string.isEmpty())
            .filter(string -> !filterBlank || !string.isBlank())
            .collect(Collectors.toList());
        return new Input(
            1 <= parts.size() ? parts.get(0) : null,
            1 <= parts.size() ? parts.subList(1, parts.size()) : Collections.emptyList()
        );
    }
}
