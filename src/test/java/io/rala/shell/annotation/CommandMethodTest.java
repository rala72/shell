package io.rala.shell.annotation;

import io.rala.shell.utils.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CommandMethodTest {
    @Test
    void commandWithoutAttributesAndMethodWithoutParameter() throws NoSuchMethodException {
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
        assertParameterCount(commandMethod, -1, 2, 0, 1);
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
        Assertions.assertEquals(
            "methodWithOneStringListParameter",
            commandMethod.getName()
        );
        Assertions.assertEquals(commandAnnotation, commandMethod.getCommand());
        Assertions.assertEquals(
            TestObject.class.getMethod(
                "methodWithOneStringListParameter",
                List.class
            ),
            commandMethod.getMethod()
        );
        Assertions.assertEquals(0, commandMethod.getMinParameterCount());
        Assertions.assertEquals(Integer.MAX_VALUE, commandMethod.getMaxParameterCount());
        assertParameterCount(commandMethod, -1, 4, 0, Integer.MAX_VALUE);
    }

    @Test
    void toStringOfEmptyCommandAndToStringMethod() throws NoSuchMethodException {
        CommandMethod commandMethod = new CommandMethod(new CommandAnnotation(), TestObject.class.getMethod("toString"));
        String toString = "CommandMethod{command=Command(value=\"\", documentation=\"\"), method=toString}";
        Assertions.assertEquals(toString, commandMethod.toString());
    }

    private void assertParameterCount(CommandMethod commandMethod, int from, int to, int requestedMin, int requestedMax) {
        for (int i = from; i < to; i++) {
            Assertions.assertEquals(
                requestedMin <= i && i < requestedMax,
                commandMethod.isParameterCountValid(i),
                String.valueOf(i));
        }
    }
}
