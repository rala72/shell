package at.rala.shell.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandMethodTest {
    @Test
    void testSimpleCommandWithoutParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod("simpleCommandWithoutParameter")
        );
        Assertions.assertEquals("simpleCommandWithoutParameter", commandMethod.getName());
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod("simpleCommandWithoutParameter"),
            commandMethod.getMethod()
        );
        Assertions.assertEquals(0, commandMethod.getMinParameterCount());
        Assertions.assertEquals(0, commandMethod.getMaxParameterCount());
        Assertions.assertFalse(commandMethod.isParameterCountValid(-1));
        Assertions.assertFalse(commandMethod.isParameterCountValid(0));
        Assertions.assertFalse(commandMethod.isParameterCountValid(1));
    }

    @Test
    void testSimpleCommandWithOneParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod("simpleCommandWithOneParameter", String.class)
        );
        Assertions.assertEquals("simpleCommandWithOneParameter", commandMethod.getName());
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod("simpleCommandWithOneParameter", String.class),
            commandMethod.getMethod()
        );
        Assertions.assertEquals(1, commandMethod.getMinParameterCount());
        Assertions.assertEquals(2, commandMethod.getMaxParameterCount());
        Assertions.assertFalse(commandMethod.isParameterCountValid(0));
        Assertions.assertTrue(commandMethod.isParameterCountValid(1));
        Assertions.assertFalse(commandMethod.isParameterCountValid(2));
    }

    @Test
    void testSimpleCommandWithTwoParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod("simpleCommandWithTwoParameter", String.class, String.class)
        );
        Assertions.assertEquals("simpleCommandWithTwoParameter", commandMethod.getName());
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod("simpleCommandWithTwoParameter", String.class, String.class),
            commandMethod.getMethod()
        );
        Assertions.assertEquals(2, commandMethod.getMinParameterCount());
        Assertions.assertEquals(3, commandMethod.getMaxParameterCount());
        Assertions.assertFalse(commandMethod.isParameterCountValid(1));
        Assertions.assertTrue(commandMethod.isParameterCountValid(2));
        Assertions.assertFalse(commandMethod.isParameterCountValid(3));
    }

    @Test
    void testSimpleCommandWithVarargsParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod("simpleCommandWithVarargsParameter", String[].class)
        );
        Assertions.assertEquals("simpleCommandWithVarargsParameter", commandMethod.getName());
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod("simpleCommandWithVarargsParameter", String[].class),
            commandMethod.getMethod()
        );
        Assertions.assertEquals(0, commandMethod.getMinParameterCount());
        Assertions.assertEquals(Integer.MAX_VALUE, commandMethod.getMaxParameterCount());
        Assertions.assertFalse(commandMethod.isParameterCountValid(-1));
        Assertions.assertTrue(commandMethod.isParameterCountValid(0));
        Assertions.assertTrue(commandMethod.isParameterCountValid(1));
        Assertions.assertTrue(commandMethod.isParameterCountValid(2));
        Assertions.assertTrue(commandMethod.isParameterCountValid(3));
        Assertions.assertTrue(commandMethod.isParameterCountValid(Integer.MAX_VALUE - 1));
        Assertions.assertFalse(commandMethod.isParameterCountValid(Integer.MAX_VALUE));
    }

    @Test
    void testToString() throws NoSuchMethodException {
        CommandMethod commandMethod = new CommandMethod(new CommandAnnotation(), TestObject.class.getMethod("toString"));
        String toString = "CommandMethod{command=Command(value=\"\", documentation=\"\"), method=toString}";
        Assertions.assertEquals(toString, commandMethod.toString());
    }
}
