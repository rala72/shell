package io.rala.shell;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link Shell} input holder
 *
 * @since 1.0.0
 */
public class Input {
    private final String command;
    private final List<String> arguments;

    /**
     * @param command   command name
     * @param arguments optional arguments
     * @since 1.0.0
     */
    public Input(@Nullable String command, @NotNull String... arguments) {
        this(command, Arrays.asList(arguments));
    }

    /**
     * @param command   command name
     * @param arguments argument list
     * @since 1.0.0
     */
    public Input(@Nullable String command, @NotNull List<String> arguments) {
        this.command = command;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    /**
     * @return command name
     * @since 1.0.0
     */
    @Nullable
    public String getCommand() {
        return command;
    }

    /**
     * @return argument list
     * @since 1.0.0
     */
    @NotNull
    @Unmodifiable
    public List<@Nullable String> getArguments() {
        return arguments;
    }

    /**
     * @return {@code true} if arguments are present
     * @since 1.0.0
     */
    public boolean hasArguments() {
        return !getArguments().isEmpty();
    }

    /**
     * @param i index of parameter
     * @return optional which has argument or is empty
     * @since 1.0.0
     */
    @NotNull
    public Optional<@NotNull String> get(int i) {
        return 0 <= i && i < getArguments().size() ?
            Optional.ofNullable(getArguments().get(i)) : Optional.empty();
    }

    /**
     * @param i index of parameter
     * @return parameter or null
     * @since 1.0.0
     */
    @Nullable
    public String getOrNull(int i) {
        return get(i).orElse(null);
    }

    @Override
    @NotNull
    public String toString() {
        return "Input{" +
            "command='" + getCommand() + '\'' +
            ", arguments=" + getArguments() +
            '}';
    }

    /**
     * @param line line to parse
     * @return new Input instance parsed by line without filtering blanks
     * @see #parse(String, boolean)
     * @since 1.0.0
     */
    @NotNull
    public static Input parse(@NotNull String line) {
        return parse(line, false);
    }

    /**
     * @param line        line to parse
     * @param filterBlank filter blank strings
     * @return new Input instance parsed by line
     * @since 1.0.0
     */
    @NotNull
    public static Input parse(@NotNull String line, boolean filterBlank) {
        List<String> parts = Stream.of(line.split(" "))
            .filter(string -> !filterBlank || !string.isEmpty())
            .collect(Collectors.toList());
        return new Input(
            !parts.isEmpty() ? parts.get(0) : null,
            !parts.isEmpty() ? parts.subList(1, parts.size()) : Collections.emptyList()
        );
    }
}
