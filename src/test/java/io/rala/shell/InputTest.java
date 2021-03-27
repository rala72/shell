package io.rala.shell;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {
    @Test
    void constructorWithoutArguments() {
        Input input = new Input("cmd");
        assertEquals("cmd", input.getCommand());
        assertFalse(input.hasArguments());
        assertTrue(input.getArguments().isEmpty());
    }

    @Test
    void constructorWithVarargsArguments() {
        Input input = new Input("cmd", "arg1", "arg2");
        assertEquals("cmd", input.getCommand());
        assertTrue(input.hasArguments());
        assertFalse(input.getArguments().isEmpty());
        assertEquals(List.of("arg1", "arg2"), input.getArguments());
    }

    @Test
    void constructorWithListArguments() {
        Input input = new Input("cmd", List.of("arg1", "arg2"));
        assertEquals("cmd", input.getCommand());
        assertTrue(input.hasArguments());
        assertFalse(input.getArguments().isEmpty());
        assertEquals(List.of("arg1", "arg2"), input.getArguments());
    }

    @Test
    void getWithoutArguments() {
        Input input = new Input("cmd");
        assertEquals("cmd", input.getCommand());
        assertFalse(input.hasArguments());
        assertTrue(input.getArguments().isEmpty());
        assertEquals(Optional.empty(), input.get(-1));
        assertNull(input.getOrNull(-1));
        assertEquals(Optional.empty(), input.get(0));
        assertNull(input.getOrNull(0));
        assertEquals(Optional.empty(), input.get(1));
        assertNull(input.getOrNull(1));
    }

    @Test
    void getWithArguments() {
        Input input = new Input("cmd", "arg");
        assertEquals("cmd", input.getCommand());
        assertTrue(input.hasArguments());
        assertFalse(input.getArguments().isEmpty());
        assertEquals(Optional.empty(), input.get(-1));
        assertNull(input.getOrNull(-1));
        assertEquals(Optional.of("arg"), input.get(0));
        assertEquals("arg", input.getOrNull(0));
        assertEquals(Optional.empty(), input.get(1));
        assertNull(input.getOrNull(1));
    }

    @Test
    void parseWithoutFilter() {
        Input input = Input.parse("this is a very  fancy line");
        assertEquals("this", input.getCommand());
        assertTrue(input.hasArguments());
        assertFalse(input.getArguments().isEmpty());
        assertEquals(List.of("is", "a", "very", "", "fancy", "line"), input.getArguments());
    }

    @Test
    void parseWithFilter() {
        Input input = Input.parse("this is a very  fancy line", true);
        assertEquals("this", input.getCommand());
        assertTrue(input.hasArguments());
        assertFalse(input.getArguments().isEmpty());
        assertEquals(List.of("is", "a", "very", "fancy", "line"), input.getArguments());
    }

    @Test
    void toStringOfCmdCommandWithoutArguments() {
        String toString = "Input{command='cmd', arguments=[]}";
        assertEquals(toString, new Input("cmd").toString());
    }
}
