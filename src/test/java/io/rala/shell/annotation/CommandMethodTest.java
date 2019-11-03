package io.rala.shell.annotation;

import io.rala.shell.utils.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandMethodTest {
    @Test
    void testCommandWithoutAttributesAndMethodWithoutParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod("methodWithoutParameter")
        );
        Assertions.assertEquals(
            "methodWithoutParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod("methodWithoutParameter"),
            commandMethod.getMethod()
        );
        Assertions.assertEquals(0, commandMethod.getMinParameterCount());
        Assertions.assertEquals(1, commandMethod.getMaxParameterCount());
        Assertions.assertFalse(commandMethod.isParameterCountValid(-1));
        Assertions.assertTrue(commandMethod.isParameterCountValid(0));
        Assertions.assertFalse(commandMethod.isParameterCountValid(1));
    }

    @Test
    void testCommandWithoutAttributesAndMethodWithOneStringParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithOneStringParameter",
                String.class
            )
        );
        Assertions.assertEquals(
            "methodWithOneStringParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "methodWithOneStringParameter",
                String.class
            ),
            commandMethod.getMethod()
        );
        Assertions.assertEquals(1, commandMethod.getMinParameterCount());
        Assertions.assertEquals(2, commandMethod.getMaxParameterCount());
        Assertions.assertFalse(commandMethod.isParameterCountValid(0));
        Assertions.assertTrue(commandMethod.isParameterCountValid(1));
        Assertions.assertFalse(commandMethod.isParameterCountValid(2));
    }

    @Test
    void testCommandWithoutAttributesAndMethodWithTwoStringParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithTwoStringParameter",
                String.class, String.class
            )
        );
        Assertions.assertEquals(
            "methodWithTwoStringParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "methodWithTwoStringParameter",
                String.class, String.class
            ),
            commandMethod.getMethod()
        );
        Assertions.assertEquals(2, commandMethod.getMinParameterCount());
        Assertions.assertEquals(3, commandMethod.getMaxParameterCount());
        Assertions.assertFalse(commandMethod.isParameterCountValid(1));
        Assertions.assertTrue(commandMethod.isParameterCountValid(2));
        Assertions.assertFalse(commandMethod.isParameterCountValid(3));
    }

    @Test
    void testCommandWithoutAttributesAndMethodWithOneStringVarargsParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithOneStringVarargsParameter",
                String[].class
            )
        );
        Assertions.assertEquals(
            "methodWithOneStringVarargsParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "methodWithOneStringVarargsParameter",
                String[].class
            ),
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
    void testCommandWithoutAttributesAndMethodWithOneStringArrayParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithOneStringArrayParameter",
                String[].class
            )
        );
        Assertions.assertEquals(
            "methodWithOneStringArrayParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "methodWithOneStringArrayParameter",
                String[].class
            ),
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
