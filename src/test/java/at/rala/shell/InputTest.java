package at.rala.shell;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

class InputTest {
    @Test
    void testConstructorWithoutArguments() {
        Input input = new Input("cmd");
        Assertions.assertEquals("cmd", input.getCommand());
        Assertions.assertFalse(input.hasArguments());
        Assertions.assertTrue(input.getArguments().isEmpty());
    }

    @Test
    void testConstructorWithVarargsArguments() {
        Input input = new Input("cmd", "arg1", "arg2");
        Assertions.assertEquals("cmd", input.getCommand());
        Assertions.assertTrue(input.hasArguments());
        Assertions.assertFalse(input.getArguments().isEmpty());
        Assertions.assertEquals(List.of("arg1", "arg2"), input.getArguments());
    }

    @Test
    void testConstructorWithListArguments() {
        Input input = new Input("cmd", List.of("arg1", "arg2"));
        Assertions.assertEquals("cmd", input.getCommand());
        Assertions.assertTrue(input.hasArguments());
        Assertions.assertFalse(input.getArguments().isEmpty());
        Assertions.assertEquals(List.of("arg1", "arg2"), input.getArguments());
    }

    @Test
    void testGetWithoutArguments() {
        Input input = new Input("cmd");
        Assertions.assertEquals("cmd", input.getCommand());
        Assertions.assertFalse(input.hasArguments());
        Assertions.assertTrue(input.getArguments().isEmpty());
        Assertions.assertEquals(Optional.empty(), input.get(-1));
        Assertions.assertEquals(Optional.empty(), input.get(0));
        Assertions.assertEquals(Optional.empty(), input.get(1));
    }

    @Test
    void testGetWithArguments() {
        Input input = new Input("cmd", "arg");
        Assertions.assertEquals("cmd", input.getCommand());
        Assertions.assertTrue(input.hasArguments());
        Assertions.assertFalse(input.getArguments().isEmpty());
        Assertions.assertEquals(Optional.empty(), input.get(-1));
        Assertions.assertEquals(Optional.of("arg"), input.get(0));
        Assertions.assertEquals(Optional.empty(), input.get(1));
    }

    @Test
    void testParseWithoutFilter() {
        Input input = Input.parse("this is a very  fancy line");
        Assertions.assertEquals("this", input.getCommand());
        Assertions.assertTrue(input.hasArguments());
        Assertions.assertFalse(input.getArguments().isEmpty());
        Assertions.assertEquals(List.of("is", "a", "very", "", "fancy", "line"), input.getArguments());
    }

    @Test
    void testParseWithFilter() {
        Input input = Input.parse("this is a very  fancy line", true);
        Assertions.assertEquals("this", input.getCommand());
        Assertions.assertTrue(input.hasArguments());
        Assertions.assertFalse(input.getArguments().isEmpty());
        Assertions.assertEquals(List.of("is", "a", "very", "fancy", "line"), input.getArguments());
    }
}
