package io.rala.shell.annotation;

import io.rala.shell.Input;
import io.rala.shell.testUtils.object.TestObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandParameterTest {
    @Test
    void methodWithOneInputParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneInputParameter",
            Input.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertEquals(1, commandParameters.length);
        assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        assertEquals("arg0", commandParameters[0].getName());
        assertEquals(Input.class, commandParameters[0].getType());
        assertFalse(commandParameters[0].isArray());
        assertFalse(commandParameters[0].isList());
        assertTrue(commandParameters[0].isDynamic());
        assertTrue(commandParameters[0].isInput());
    }

    @Test
    void methodWithOneStringParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringParameter",
            String.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertEquals(1, commandParameters.length);
        assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        assertEquals("arg0", commandParameters[0].getName());
        assertEquals(String.class, commandParameters[0].getType());
        assertFalse(commandParameters[0].isArray());
        assertFalse(commandParameters[0].isList());
        assertFalse(commandParameters[0].isDynamic());
        assertFalse(commandParameters[0].isInput());
    }

    @Test
    void methodWithTwoStringParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithTwoStringParameter",
            String.class, String.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertEquals(2, commandParameters.length);
        for (int i = 0; i < 2; i++) {
            assertEquals(
                method.getParameters()[i],
                commandParameters[i].getParameter()
            );
            assertEquals("arg" + i, commandParameters[i].getName());
            assertEquals(String.class, commandParameters[i].getType());
            assertFalse(commandParameters[i].isArray());
            assertFalse(commandParameters[i].isList());
            assertFalse(commandParameters[i].isDynamic());
            assertFalse(commandParameters[i].isInput());
        }
    }

    @Test
    void methodWithOneStringVarargsParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringVarargsParameter",
            String[].class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertEquals(1, commandParameters.length);
        assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        assertEquals("arg0", commandParameters[0].getName());
        assertEquals(String[].class, commandParameters[0].getType());
        assertTrue(commandParameters[0].isArray());
        assertFalse(commandParameters[0].isList());
        assertTrue(commandParameters[0].isDynamic());
        assertFalse(commandParameters[0].isInput());
    }

    @Test
    void methodWithOneStringArrayParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringArrayParameter",
            String[].class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertEquals(1, commandParameters.length);
        assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        assertEquals("arg0", commandParameters[0].getName());
        assertEquals(String[].class, commandParameters[0].getType());
        assertTrue(commandParameters[0].isArray());
        assertFalse(commandParameters[0].isList());
        assertTrue(commandParameters[0].isDynamic());
        assertFalse(commandParameters[0].isInput());
    }

    @Test
    void methodWithOneStringListParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringListParameter",
            List.class
        );
        CommandParameter[] commandParameters = extractCommandParameters(method);
        assertEquals(1, commandParameters.length);
        assertEquals(
            method.getParameters()[0],
            commandParameters[0].getParameter()
        );
        assertEquals(List.class, commandParameters[0].getType());
        assertFalse(commandParameters[0].isArray());
        assertTrue(commandParameters[0].isList());
        assertTrue(commandParameters[0].isDynamic());
        assertFalse(commandParameters[0].isInput());
    }

    @Test
    void toStringOfStringParameter() throws NoSuchMethodException {
        Method method = TestObject.class.getMethod(
            "methodWithOneStringParameter",
            String.class
        );
        CommandParameter commandParameter = extractCommandParameters(method)[0];
        String toString = "java.lang.String arg0";
        assertEquals(toString, commandParameter.toString());
    }

    private static CommandParameter[] extractCommandParameters(Method method) {
        return Arrays.stream(method.getParameters())
            .map(CommandParameter::new)
            .toArray(CommandParameter[]::new);
    }
}
