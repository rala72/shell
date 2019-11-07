package io.rala.shell.annotation;

import io.rala.shell.Input;
import io.rala.shell.utils.object.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class CommandParameterTest {
    @Test
    void methodWithOneInputParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneInputParameter",
            Input.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        Assertions.assertEquals(1, commandParameters.length);
        Assertions.assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        Assertions.assertEquals(Input.class, commandParameters[0].getType());
        Assertions.assertFalse(commandParameters[0].isArray());
        Assertions.assertFalse(commandParameters[0].isList());
        Assertions.assertTrue(commandParameters[0].isDynamic());
        Assertions.assertTrue(commandParameters[0].isInput());
    }

    @Test
    void methodWithOneStringParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringParameter",
            String.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        Assertions.assertEquals(1, commandParameters.length);
        Assertions.assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        Assertions.assertEquals(String.class, commandParameters[0].getType());
        Assertions.assertFalse(commandParameters[0].isArray());
        Assertions.assertFalse(commandParameters[0].isList());
        Assertions.assertFalse(commandParameters[0].isDynamic());
        Assertions.assertFalse(commandParameters[0].isInput());
    }

    @Test
    void methodWithTwoStringParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithTwoStringParameter",
            String.class, String.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        Assertions.assertEquals(2, commandParameters.length);
        for (int i = 0; i < 2; i++) {
            Assertions.assertEquals(
                method.getParameters()[i],
                commandParameters[i].getParameter()
            );
            Assertions.assertEquals(String.class, commandParameters[i].getType());
            Assertions.assertFalse(commandParameters[i].isArray());
            Assertions.assertFalse(commandParameters[i].isList());
            Assertions.assertFalse(commandParameters[i].isDynamic());
            Assertions.assertFalse(commandParameters[i].isInput());
        }
    }

    @Test
    void methodWithOneStringVarargsParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringVarargsParameter",
            String[].class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        Assertions.assertEquals(1, commandParameters.length);
        Assertions.assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        Assertions.assertEquals(String[].class, commandParameters[0].getType());
        Assertions.assertTrue(commandParameters[0].isArray());
        Assertions.assertFalse(commandParameters[0].isList());
        Assertions.assertTrue(commandParameters[0].isDynamic());
        Assertions.assertFalse(commandParameters[0].isInput());
    }

    @Test
    void methodWithOneStringArrayParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringArrayParameter",
            String[].class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        Assertions.assertEquals(1, commandParameters.length);
        Assertions.assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        Assertions.assertEquals(String[].class, commandParameters[0].getType());
        Assertions.assertTrue(commandParameters[0].isArray());
        Assertions.assertFalse(commandParameters[0].isList());
        Assertions.assertTrue(commandParameters[0].isDynamic());
        Assertions.assertFalse(commandParameters[0].isInput());
    }

    @Test
    void methodWithOneStringListParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringListParameter",
            List.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        Assertions.assertEquals(1, commandParameters.length);
        Assertions.assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        Assertions.assertEquals(List.class, commandParameters[0].getType());
        Assertions.assertFalse(commandParameters[0].isArray());
        Assertions.assertTrue(commandParameters[0].isList());
        Assertions.assertTrue(commandParameters[0].isDynamic());
        Assertions.assertFalse(commandParameters[0].isInput());
    }

    @Test
    void toStringOfStringParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringParameter",
            String.class
        );
        CommandParameter commandParameter = extractCommandParameters(method)[0];
        String toString = "java.lang.String arg0";
        Assertions.assertEquals(toString, commandParameter.toString());
    }

    private static CommandParameter[] extractCommandParameters(Method method) {
        return Arrays.stream(method.getParameters())
            .map(CommandParameter::new)
            .toArray(CommandParameter[]::new);
    }
}
