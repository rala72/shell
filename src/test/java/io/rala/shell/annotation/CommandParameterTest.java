package io.rala.shell.annotation;

import io.rala.shell.Input;
import io.rala.shell.testUtils.object.TestObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandParameterTest {
    @Test
    void methodWithOneInputParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneInputParameter",
            Input.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertThat(commandParameters).hasSize(1);
        assertThat(commandParameters[0].getParameter()).isEqualTo(method.getParameters()[0]);
        assertThat(commandParameters[0].getName()).isEqualTo("arg0");
        assertThat(commandParameters[0].getType()).isEqualTo(Input.class);
        assertThat(commandParameters[0].isArray()).isFalse();
        assertThat(commandParameters[0].isList()).isFalse();
        assertThat(commandParameters[0].isDynamic()).isTrue();
        assertThat(commandParameters[0].isInput()).isTrue();
    }

    @Test
    void methodWithOneStringParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringParameter",
            String.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertThat(commandParameters).hasSize(1);
        assertThat(commandParameters[0].getParameter()).isEqualTo(method.getParameters()[0]);
        assertThat(commandParameters[0].getName()).isEqualTo("arg0");
        assertThat(commandParameters[0].getType()).isEqualTo(String.class);
        assertThat(commandParameters[0].isArray()).isFalse();
        assertThat(commandParameters[0].isList()).isFalse();
        assertThat(commandParameters[0].isDynamic()).isFalse();
        assertThat(commandParameters[0].isInput()).isFalse();
    }

    @Test
    void methodWithTwoStringParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithTwoStringParameter",
            String.class, String.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertThat(commandParameters).hasSize(2);
        for (int i = 0; i < 2; i++) {
            assertThat(commandParameters[i].getParameter()).isEqualTo(method.getParameters()[i]);
            assertThat(commandParameters[i].getName()).isEqualTo("arg" + i);
            assertThat(commandParameters[i].getType()).isEqualTo(String.class);
            assertThat(commandParameters[i].isArray()).isFalse();
            assertThat(commandParameters[i].isList()).isFalse();
            assertThat(commandParameters[i].isDynamic()).isFalse();
            assertThat(commandParameters[i].isInput()).isFalse();
        }
    }

    @Test
    void methodWithOneStringVarargsParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringVarargsParameter",
            String[].class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertThat(commandParameters).hasSize(1);
        assertThat(commandParameters[0].getParameter()).isEqualTo(method.getParameters()[0]);
        assertThat(commandParameters[0].getName()).isEqualTo("arg0");
        assertThat(commandParameters[0].getType()).isEqualTo(String[].class);
        assertThat(commandParameters[0].isArray()).isTrue();
        assertThat(commandParameters[0].isList()).isFalse();
        assertThat(commandParameters[0].isDynamic()).isTrue();
        assertThat(commandParameters[0].isInput()).isFalse();
    }

    @Test
    void methodWithOneStringArrayParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringArrayParameter",
            String[].class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertThat(commandParameters).hasSize(1);
        assertThat(commandParameters[0].getParameter()).isEqualTo(method.getParameters()[0]);
        assertThat(commandParameters[0].getName()).isEqualTo("arg0");
        assertThat(commandParameters[0].getType()).isEqualTo(String[].class);
        assertThat(commandParameters[0].isArray()).isTrue();
        assertThat(commandParameters[0].isList()).isFalse();
        assertThat(commandParameters[0].isDynamic()).isTrue();
        assertThat(commandParameters[0].isInput()).isFalse();
    }

    @Test
    void methodWithOneStringListParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringListParameter",
            List.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertThat(commandParameters).hasSize(1);
        assertThat(commandParameters[0].getParameter()).isEqualTo(method.getParameters()[0]);
        assertThat(commandParameters[0].getType()).isEqualTo(List.class);
        assertThat(commandParameters[0].isArray()).isFalse();
        assertThat(commandParameters[0].isList()).isTrue();
        assertThat(commandParameters[0].isDynamic()).isTrue();
        assertThat(commandParameters[0].isInput()).isFalse();
    }

    @Test
    void toStringOfStringParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringParameter",
            String.class
        );
        CommandParameter commandParameter = extractCommandParameters(method)[0];
        String toString = "java.lang.String arg0";
        assertThat(commandParameter).hasToString(toString);
    }

    private static CommandParameter[] extractCommandParameters(Method method) {
        return Arrays.stream(method.getParameters())
            .map(CommandParameter::new)
            .toArray(CommandParameter[]::new);
    }
}
