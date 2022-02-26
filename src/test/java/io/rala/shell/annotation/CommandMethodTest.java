package io.rala.shell.annotation;

import io.rala.shell.Input;
import io.rala.shell.testUtils.object.TestObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandMethodTest {
    @Test
    void commandWithoutAttributesAndMethodWithoutParameter() throws NoSuchMethodException {
        CommandAnnotation commandAnnotation = new CommandAnnotation();
        CommandMethod commandMethod = new CommandMethod(
            commandAnnotation,
            TestObject.class.getMethod("methodWithoutParameter")
        );
        assertThat(commandMethod.getName()).isEqualTo("methodWithoutParameter");
        assertThat(commandMethod.getCommand()).isEqualTo(commandAnnotation);
        assertThat(commandMethod.getMethod()).isEqualTo(TestObject.class.getMethod("methodWithoutParameter"));
        assertThat(commandMethod.getMinParameterCount()).isZero();
        assertThat(commandMethod.getMaxParameterCount()).isEqualTo(1);
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
        assertThat(commandMethod.getName()).isEqualTo("methodWithOneInputParameter");
        assertThat(commandMethod.getCommand()).isEqualTo(commandAnnotation);
        assertThat(commandMethod.getMethod()).isEqualTo(TestObject.class.getMethod(
            "methodWithOneInputParameter",
            Input.class
        ));
        assertThat(commandMethod.getMinParameterCount()).isZero();
        assertThat(commandMethod.getMaxParameterCount()).isEqualTo(Integer.MAX_VALUE);
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
        assertThat(commandMethod.getName()).isEqualTo("methodWithOneStringParameter");
        assertThat(commandMethod.getCommand()).isEqualTo(commandAnnotation);
        assertThat(commandMethod.getMethod()).isEqualTo(TestObject.class.getMethod(
            "methodWithOneStringParameter",
            String.class
        ));
        assertThat(commandMethod.getMinParameterCount()).isEqualTo(1);
        assertThat(commandMethod.getMaxParameterCount()).isEqualTo(2);
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
        assertThat(commandMethod.getName()).isEqualTo("methodWithTwoStringParameter");
        assertThat(commandMethod.getCommand()).isEqualTo(commandAnnotation);
        assertThat(commandMethod.getMethod()).isEqualTo(TestObject.class.getMethod(
            "methodWithTwoStringParameter",
            String.class, String.class
        ));
        assertThat(commandMethod.getMinParameterCount()).isEqualTo(2);
        assertThat(commandMethod.getMaxParameterCount()).isEqualTo(3);
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
        assertThat(commandMethod.getName()).isEqualTo("methodWithOneStringVarargsParameter");
        assertThat(commandMethod.getCommand()).isEqualTo(commandAnnotation);
        assertThat(commandMethod.getMethod()).isEqualTo(TestObject.class.getMethod(
            "methodWithOneStringVarargsParameter",
            String[].class
        ));
        assertThat(commandMethod.getMinParameterCount()).isZero();
        assertThat(commandMethod.getMaxParameterCount()).isEqualTo(Integer.MAX_VALUE);
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
        assertThat(commandMethod.getName()).isEqualTo("methodWithOneStringArrayParameter");
        assertThat(commandMethod.getCommand()).isEqualTo(commandAnnotation);
        assertThat(commandMethod.getMethod()).isEqualTo(TestObject.class.getMethod(
            "methodWithOneStringArrayParameter",
            String[].class
        ));
        assertThat(commandMethod.getMinParameterCount()).isZero();
        assertThat(commandMethod.getMaxParameterCount()).isEqualTo(Integer.MAX_VALUE);
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
        assertThat(commandMethod.getName()).isEqualTo("methodWithOneStringListParameter");
        assertThat(commandMethod.getCommand()).isEqualTo(commandAnnotation);
        assertThat(commandMethod.getMethod()).isEqualTo(TestObject.class.getMethod(
            "methodWithOneStringListParameter",
            List.class
        ));
        assertThat(commandMethod.getMinParameterCount()).isZero();
        assertThat(commandMethod.getMaxParameterCount()).isEqualTo(Integer.MAX_VALUE);
        assertParameterCount(commandMethod, -1, 4, 0, Integer.MAX_VALUE);
    }

    @Test
    void toStringOfEmptyCommandAndToStringMethod() throws NoSuchMethodException {
        CommandMethod commandMethod = new CommandMethod(new CommandAnnotation(),
            TestObject.class.getMethod("toString"));
        String toString = "CommandMethod{command=Command(value=\"\", documentation=\"\"), method=toString}";
        assertThat(commandMethod).hasToString(toString);
    }

    private void assertParameterCount(CommandMethod commandMethod,
                                      int from, int to,
                                      int requestedMin, int requestedMax) {
        for (int i = from; i < to; i++) {
            assertThat(commandMethod.isParameterCountValid(i)).as(String.valueOf(i))
                .isEqualTo(requestedMin <= i && i < requestedMax);
        }
    }
}
