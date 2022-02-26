package io.rala.shell;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InputTest {
    @Test
    void constructorWithoutArguments() {
        Input input = new Input("cmd");
        assertThat(input.getCommand()).isEqualTo("cmd");
        assertThat(input.hasArguments()).isFalse();
        assertThat(input.getArguments()).isEmpty();
    }

    @Test
    void constructorWithVarargsArguments() {
        Input input = new Input("cmd", "arg1", "arg2");
        assertThat(input.getCommand()).isEqualTo("cmd");
        assertThat(input.hasArguments()).isTrue();
        assertThat(input.getArguments()).isNotEmpty()
            .isEqualTo(List.of("arg1", "arg2"));
    }

    @Test
    void constructorWithListArguments() {
        Input input = new Input("cmd", List.of("arg1", "arg2"));
        assertThat(input.getCommand()).isEqualTo("cmd");
        assertThat(input.hasArguments()).isTrue();
        assertThat(input.getArguments())
            .isNotEmpty()
            .isEqualTo(List.of("arg1", "arg2"));
    }

    @Test
    void getWithoutArguments() {
        Input input = new Input("cmd");
        assertThat(input.getCommand()).isEqualTo("cmd");
        assertThat(input.hasArguments()).isFalse();
        assertThat(input.getArguments()).isEmpty();
        assertThat(input.get(-1)).isNotPresent();
        assertThat(input.getOrNull(-1)).isNull();
        assertThat(input.get(0)).isNotPresent();
        assertThat(input.getOrNull(0)).isNull();
        assertThat(input.get(1)).isNotPresent();
        assertThat(input.getOrNull(1)).isNull();
    }

    @Test
    void getWithArguments() {
        Input input = new Input("cmd", "arg");
        assertThat(input.getCommand()).isEqualTo("cmd");
        assertThat(input.hasArguments()).isTrue();
        assertThat(input.getArguments()).isNotEmpty();
        assertThat(input.get(-1)).isNotPresent();
        assertThat(input.getOrNull(-1)).isNull();
        assertThat(input.get(0)).contains("arg");
        assertThat(input.getOrNull(0)).isEqualTo("arg");
        assertThat(input.get(1)).isNotPresent();
        assertThat(input.getOrNull(1)).isNull();
    }

    @Test
    void parseWithoutFilter() {
        Input input = Input.parse("this is a very  fancy line");
        assertThat(input.getCommand()).isEqualTo("this");
        assertThat(input.hasArguments()).isTrue();
        assertThat(input.getArguments())
            .isNotEmpty().isEqualTo(List.of("is", "a", "very", "", "fancy", "line"));
    }

    @Test
    void parseWithFilter() {
        Input input = Input.parse("this is a very  fancy line", true);
        assertThat(input.getCommand()).isEqualTo("this");
        assertThat(input.hasArguments()).isTrue();
        assertThat(input.getArguments())
            .isNotEmpty().isEqualTo(List.of("is", "a", "very", "fancy", "line"));
    }

    @Test
    void toStringOfCmdCommandWithoutArguments() {
        String toString = "Input{command='cmd', arguments=[]}";
        assertThat(new Input("cmd")).hasToString(toString);
    }
}
