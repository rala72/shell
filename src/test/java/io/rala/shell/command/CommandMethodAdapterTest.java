package io.rala.shell.command;

import io.rala.StringMapper;
import io.rala.shell.Input;
import io.rala.shell.annotation.CommandAnnotation;
import io.rala.shell.annotation.CommandLoader;
import io.rala.shell.annotation.CommandMethod;
import io.rala.shell.exception.MethodCallException;
import io.rala.shell.testUtils.TestContext;
import io.rala.shell.testUtils.arguments.ParameterArgumentsStreamFactory;
import io.rala.shell.testUtils.object.TestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CommandMethodAdapterTest {
    private TestContext context;

    @BeforeEach
    void setUp() {
        TestObject object = new TestObject();
        CommandLoader commandLoader = new CommandLoader(object, new StringMapper());
        context = TestContext.getInstanceWithDifferentStreams(commandLoader.getCommandMethodMap());
        object.setContext(context);
    }

    // region documentation & usage

    @Test
    void documentationOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        assertNotNull(command.getDocumentation());
        assertTrue(command.getDocumentation().isBlank());
    }

    @Test
    void usageOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        assertNotNull(command.getUsage());
        assertTrue(command.getUsage().isBlank());
    }

    @Test
    void documentationOfCommandWithAttributes() {
        assertNull(getCommand("commandWithAttributes"));
        Command command = getCommand("value");
        assertNotNull(command);
        assertNotNull(command.getDocumentation());
        assertFalse(command.getDocumentation().isBlank());
        assertEquals("documentation", command.getDocumentation());
    }

    @Test
    void usageOfCommandWithAttributes() {
        assertNull(getCommand("commandWithAttributes"));
        Command command = getCommand("value");
        assertNotNull(command);
        assertNotNull(command.getUsage());
        assertFalse(command.getUsage().isBlank());
        assertEquals("usage", command.getUsage());
    }

    // endregion

    @Test
    void commandWithContextParameter() {
        executeCommand(new Input("methodWithContextParameter"));
        assertOutputsAreEmpty();
    }

    @ParameterizedTest
    @MethodSource("getStringArguments")
    void commandWithoutAttributes(Input input, Integer minExpected, Integer maxExpected) {
        executeCommand(input);
        if (maxExpected == null) maxExpected = Integer.MAX_VALUE;
        if (minExpected == null ||
            minExpected <= input.getArguments().size() &&
                input.getArguments().size() <= maxExpected)
            assertOutputsAreEmpty();
        else assertErrorOutputContainsExpectedArgumentCount(minExpected, maxExpected);
    }

    @ParameterizedTest
    @MethodSource("getValidMappingArguments")
    void commandWithValidParameterMapping(String name, boolean isPrimitive, Input input) {
        String command = input.getCommand();
        assertNotNull(command);
        assertTrue(command.contains(name));
        assertTrue(command.contains(isPrimitive ? "Primitive" : "Object"));
        assertEquals(1, input.getArguments().size(), "config error");

        executeCommand(input);

        String argument = input.getOrNull(0);
        assertNotNull(argument);
        assertArgumentPresentInOutput(argument);
    }

    @ParameterizedTest
    @MethodSource("getInvalidMappingArguments")
    void commandWithInvalidParameterMapping(String name, boolean isPrimitive, Input input) {
        String command = input.getCommand();
        assertNotNull(command);
        assertTrue(command.contains(name));
        assertTrue(command.contains(isPrimitive ? "Primitive" : "Object"));
        assertEquals(1, input.getArguments().size(), "config error");

        String argument = input.getOrNull(0);
        assertNotNull(argument);
        if (name.equals("Boolean") && (isPrimitive || !argument.equals("null"))) {
            executeCommand(input);
            assertArgumentPresentInOutput("false");
        } else if (!isPrimitive && argument.equals("null")) {
            executeCommand(input);
            assertArgumentPresentInOutput(argument);
        } else {
            assertThrows(MethodCallException.class,
                () -> executeCommand(input)
            );
        }
    }

    @ParameterizedTest
    @MethodSource("getOptionalDefaultValueArguments")
    void commandWithOptionalParameterWithDefaultValue(Input input, String defaultValue) {
        executeCommand(input);
        assertArgumentPresentInOutput(defaultValue);
    }

    @ParameterizedTest
    @MethodSource("getReturnValueArguments")
    void commandWithParameterWitchReturns(Input input, String expected) {
        executeCommand(input);
        assertArgumentPresentInOutput(expected);
    }

    // region execute exception

    @Test
    void commandWithListParameterAndNull() {
        try {
            executeCommand(new Input("methodWithOneStringListParameter", "null"));
            fail();
        } catch (MethodCallException e) {
            assertEquals("java.lang.NullPointerException", e.getMessage());
        }
    }

    @Test
    void exceptionCommandWithoutAttributes() {
        try {
            executeCommand(new Input("exceptionCommandWithoutMessage"));
            fail();
        } catch (MethodCallException e) {
            assertTrue(e.getCause() instanceof InvocationTargetException);
            InvocationTargetException cause = (InvocationTargetException) e.getCause();
            assertNull(cause.getMessage());
        }
    }

    @Test
    void illegalAccessCommandWithoutAttributes() throws NoSuchMethodException {
        CommandMethodAdapter illegalAccessCommand = new CommandMethodAdapter(
            new TestObject(),
            new CommandMethod(new CommandAnnotation(),
                TestObject.class.getDeclaredMethod("illegalAccessCommand")
            )
        );
        assertThrows(MethodCallException.class,
            () -> illegalAccessCommand.execute(new Input("illegalAccessCommand"), context)
        );
    }

    // endregion

    @Test
    void toStringOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        String toString = "CommandMethodAdapter{object=TestObject, " +
            "commandMethod=CommandMethod{" +
            "command=Command(value=\"\", documentation=\"\"), " +
            "method=methodWithoutParameter}}";
        assertEquals(toString, command.toString());
    }

    // region assert

    private void assertOutputsAreEmpty() {
        assertTrue(context.getOutputHistory().isEmpty(),
            "output history is not empty"
        );
        assertTrue(context.getErrorHistory().isEmpty(),
            "error history is not empty"
        );
    }

    private void assertErrorOutputContainsExpectedArgumentCount(int min, int max) {
        assertTrue(context.getOutputHistory().isEmpty());
        assertFalse(context.getErrorHistory().isEmpty());
        String count = min + "" + (min == max ? "" : "-" +
            (max == Integer.MAX_VALUE ? '∞' : max)
        );
        assertTrue(context.getErrorHistory().contains(
            "error: expected argument count: " + count
        ));
    }

    private void assertArgumentPresentInOutput(String argument) {
        assertTrue(
            context.getOutputHistory().contains(argument),
            String.format("'%s' not present", argument)
        );
    }

    // endregion
    // region command

    private void executeCommand(Input input) {
        Command command = getCommand(input.getCommand());
        assertNotNull(command, "command not found: " + input.getCommand());
        command.execute(input, context);
    }

    private Command getCommand(String name) {
        return context.getCommands().get(name);
    }

    // endregion
    // region arguments stream

    private static Stream<Arguments> getStringArguments() {
        return ParameterArgumentsStreamFactory.methodString();
    }

    private static Stream<Arguments> getValidMappingArguments() {
        return ParameterArgumentsStreamFactory.methodValidMapping();
    }

    private static Stream<Arguments> getInvalidMappingArguments() {
        return ParameterArgumentsStreamFactory.methodInvalidMapping();
    }

    private static Stream<Arguments> getOptionalDefaultValueArguments() {
        return ParameterArgumentsStreamFactory.methodOptionalWithDefaultValue();
    }

    private static Stream<Arguments> getReturnValueArguments() {
        return ParameterArgumentsStreamFactory.methodWithReturnValue();
    }

    // endregion
}
