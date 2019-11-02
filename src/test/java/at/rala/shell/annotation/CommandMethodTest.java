package at.rala.shell.annotation;

import at.rala.shell.utils.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandMethodTest {
    @Test
    void testCommandWithoutAttributesAndMethodWithoutParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "commandWithoutAttributesAndMethodWithoutParameter"
            )
        );
        Assertions.assertEquals(
            "commandWithoutAttributesAndMethodWithoutParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "commandWithoutAttributesAndMethodWithoutParameter"
            ),
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
                "commandWithoutAttributesAndMethodWithOneStringParameter",
                String.class
            )
        );
        Assertions.assertEquals(
            "commandWithoutAttributesAndMethodWithOneStringParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "commandWithoutAttributesAndMethodWithOneStringParameter",
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
                "commandWithoutAttributesAndMethodWithTwoStringParameter",
                String.class, String.class
            )
        );
        Assertions.assertEquals(
            "commandWithoutAttributesAndMethodWithTwoStringParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "commandWithoutAttributesAndMethodWithTwoStringParameter",
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
                "commandWithoutAttributesAndMethodWithOneStringVarargsParameter",
                String[].class
            )
        );
        Assertions.assertEquals(
            "commandWithoutAttributesAndMethodWithOneStringVarargsParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "commandWithoutAttributesAndMethodWithOneStringVarargsParameter",
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
                "commandWithoutAttributesAndMethodWithOneStringArrayParameter",
                String[].class
            )
        );
        Assertions.assertEquals(
            "commandWithoutAttributesAndMethodWithOneStringArrayParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "commandWithoutAttributesAndMethodWithOneStringArrayParameter",
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
