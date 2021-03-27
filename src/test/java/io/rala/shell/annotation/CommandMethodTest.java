package io.rala.shell.annotation;

import io.rala.shell.Input;
import io.rala.shell.testUtils.object.TestObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandMethodTest {
    @Test
    void commandWithoutAttributesAndMethodWithoutParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod("methodWithoutParameter")
        );
        assertEquals(
            "methodWithoutParameter",
            commandMethod.getName()
        );
        assertEquals(commandAnnotation, commandMethod.getCommand());
        assertEquals(
            TestObject.class.getMethod("methodWithoutParameter"),
            commandMethod.getMethod()
        );
        assertEquals(0, commandMethod.getMinParameterCount());
        assertEquals(1, commandMethod.getMaxParameterCount());
        assertParameterCount(commandMethod, -1, 2, 0, 1);
    }

    @Test
    void commandWithoutAttributesAndMethodWithOneInputParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithOneInputParameter",
                Input.class
            )
        );
        assertEquals(
            "methodWithOneInputParameter",
            commandMethod.getName()
        );
        assertEquals(commandAnnotation, commandMethod.getCommand());
        assertEquals(
            TestObject.class.getMethod(
                "methodWithOneInputParameter",
                Input.class
            ),
            commandMethod.getMethod()
        );
        assertEquals(0, commandMethod.getMinParameterCount());
        assertEquals(Integer.MAX_VALUE, commandMethod.getMaxParameterCount());
        assertParameterCount(commandMethod, -1, 4, 0, Integer.MAX_VALUE);
    }

    @Test
    void commandWithoutAttributesAndMethodWithOneStringParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithOneStringParameter",
                String.class
            )
        );
        assertEquals(
            "methodWithOneStringParameter",
            commandMethod.getName()
        );
        assertEquals(commandAnnotation, commandMethod.getCommand());
        assertEquals(
            TestObject.class.getMethod(
                "methodWithOneStringParameter",
                String.class
            ),
            commandMethod.getMethod()
        );
        assertEquals(1, commandMethod.getMinParameterCount());
        assertEquals(2, commandMethod.getMaxParameterCount());
        assertParameterCount(commandMethod, 0, 3, 1, 2);
    }

    @Test
    void commandWithoutAttributesAndMethodWithTwoStringParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithTwoStringParameter",
                String.class, String.class
            )
        );
        assertEquals(
            "methodWithTwoStringParameter",
            commandMethod.getName()
        );
        assertEquals(commandAnnotation, commandMethod.getCommand());
        assertEquals(
            TestObject.class.getMethod(
                "methodWithTwoStringParameter",
                String.class, String.class
            ),
            commandMethod.getMethod()
        );
        assertEquals(2, commandMethod.getMinParameterCount());
        assertEquals(3, commandMethod.getMaxParameterCount());
        assertParameterCount(commandMethod, 0, 4, 2, 3);
    }

    @Test
    void commandWithoutAttributesAndMethodWithOneStringVarargsParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithOneStringVarargsParameter",
                String[].class
            )
        );
        assertEquals(
            "methodWithOneStringVarargsParameter",
            commandMethod.getName()
        );
        assertEquals(commandAnnotation, commandMethod.getCommand());
        assertEquals(
            TestObject.class.getMethod(
                "methodWithOneStringVarargsParameter",
                String[].class
            ),
            commandMethod.getMethod()
        );
        assertEquals(0, commandMethod.getMinParameterCount());
        assertEquals(Integer.MAX_VALUE, commandMethod.getMaxParameterCount());
        assertParameterCount(commandMethod, -1, 4, 0, Integer.MAX_VALUE);
    }

    @Test
    void commandWithoutAttributesAndMethodWithOneStringArrayParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithOneStringArrayParameter",
                String[].class
            )
        );
        assertEquals(
            "methodWithOneStringArrayParameter",
            commandMethod.getName()
        );
        assertEquals(commandAnnotation, commandMethod.getCommand());
        assertEquals(
            TestObject.class.getMethod(
                "methodWithOneStringArrayParameter",
                String[].class
            ),
            commandMethod.getMethod()
        );
        assertEquals(0, commandMethod.getMinParameterCount());
        assertEquals(Integer.MAX_VALUE, commandMethod.getMaxParameterCount());
        assertParameterCount(commandMethod, -1, 4, 0, Integer.MAX_VALUE);
    }

    @Test
    void commandWithoutAttributesAndMethodWithOneStringListParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod(
                "methodWithOneStringListParameter",
                List.class
            )
        );
        assertEquals(
            "methodWithOneStringListParameter",
            commandMethod.getName()
        );
        assertEquals(commandAnnotation, commandMethod.getCommand());
        assertEquals(
            TestObject.class.getMethod(
                "methodWithOneStringListParameter",
                List.class
            ),
            commandMethod.getMethod()
        );
        assertEquals(0, commandMethod.getMinParameterCount());
        assertEquals(Integer.MAX_VALUE, commandMethod.getMaxParameterCount());
        assertParameterCount(commandMethod, -1, 4, 0, Integer.MAX_VALUE);
    }

    @Test
    void toStringOfEmptyCommandAndToStringMethod() throws NoSuchMethodException {
        CommandMethod commandMethod = new CommandMethod(new CommandAnnotation(), TestObject.class.getMethod("toString"));
        String toString = "CommandMethod{command=Command(value=\"\", documentation=\"\"), method=toString}";
        assertEquals(toString, commandMethod.toString());
    }

    private void assertParameterCount(CommandMethod commandMethod, int from, int to, int requestedMin, int requestedMax) {
        for (int i = from; i < to; i++) {
            assertEquals(
                requestedMin <= i && i < requestedMax,
                commandMethod.isParameterCountValid(i),
                String.valueOf(i));
        }
    }
}
