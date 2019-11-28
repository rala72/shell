package io.rala.shell.command;

import io.rala.shell.Input;
import io.rala.shell.annotation.CommandAnnotation;
import io.rala.shell.annotation.CommandLoader;
import io.rala.shell.annotation.CommandMethod;
import io.rala.shell.exception.MethodCallException;
import io.rala.shell.testUtils.TestContext;
import io.rala.shell.testUtils.arguments.ParameterArgumentsStreamFactory;
import io.rala.shell.testUtils.object.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

class CommandMethodAdapterTest {
    private TestContext context;

    @BeforeEach
    void setUp() {
        TestObject object = new TestObject();
        CommandLoader commandLoader = new CommandLoader(object);
        context = TestContext.getInstanceWithDifferentStreams(commandLoader.getCommandMethodMap());
        object.setContext(context);
    }

    // region documentation & usage

    @Test
    void documentationOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        Assertions.assertTrue(command.getDocumentation().isBlank());
    }

    @Test
    void usageOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        Assertions.assertTrue(command.getUsage().isBlank());
    }

    @Test
    void documentationOfCommandWithAttributes() {
        Assertions.assertNull(getCommand("commandWithAttributes"));
        Command command = getCommand("value");
        Assertions.assertNotNull(command);
        Assertions.assertFalse(command.getDocumentation().isBlank());
        Assertions.assertEquals("documentation", command.getDocumentation());
    }

    @Test
    void usageOfCommandWithAttributes() {
        Assertions.assertNull(getCommand("commandWithAttributes"));
        Command command = getCommand("value");
        Assertions.assertNotNull(command);
        Assertions.assertFalse(command.getUsage().isBlank());
        Assertions.assertEquals("usage", command.getUsage());
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
        Assertions.assertTrue(command.contains(name));
        Assertions.assertTrue(command.contains(isPrimitive ? "Primitive" : "Object"));
        Assertions.assertEquals(1, input.getArguments().size(), "config error");

        executeCommand(input);

        String argument = input.getOrNull(0);
        Assertions.assertNotNull(argument);
        assertArgumentPresentInOutput(argument);
    }

    @ParameterizedTest
    @MethodSource("getInvalidMappingArguments")
    void commandWithInvalidParameterMapping(String name, boolean isPrimitive, Input input) {
        String command = input.getCommand();
        Assertions.assertTrue(command.contains(name));
        Assertions.assertTrue(command.contains(isPrimitive ? "Primitive" : "Object"));
        Assertions.assertEquals(1, input.getArguments().size(), "config error");

        String argument = input.getOrNull(0);
        Assertions.assertNotNull(argument);
        if (name.equals("Boolean") && (isPrimitive || !argument.equals("null"))) {
            executeCommand(input);
            assertArgumentPresentInOutput("false");
        } else if (!isPrimitive && argument.equals("null")) {
            executeCommand(input);
            assertArgumentPresentInOutput(argument);
        } else {
            Assertions.assertThrows(MethodCallException.class,
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
            Assertions.fail();
        } catch (MethodCallException e) {
            Assertions.assertEquals("java.lang.NullPointerException", e.getMessage());
        }
    }

    @Test
    void exceptionCommandWithoutAttributes() {
        try {
            executeCommand(new Input("exceptionCommandWithoutMessage"));
            Assertions.fail();
        } catch (MethodCallException e) {
            Assertions.assertTrue(e.getCause() instanceof InvocationTargetException);
            InvocationTargetException cause = (InvocationTargetException) e.getCause();
            Assertions.assertNull(cause.getMessage());
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
        Assertions.assertThrows(MethodCallException.class,
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
        Assertions.assertEquals(toString, command.toString());
    }

    // region assert

    private void assertOutputsAreEmpty() {
        Assertions.assertTrue(context.getOutputHistory().isEmpty(),
            "output history is not empty"
        );
        Assertions.assertTrue(context.getErrorHistory().isEmpty(),
            "error history is not empty"
        );
    }

    private void assertErrorOutputContainsExpectedArgumentCount(int min, int max) {
        Assertions.assertTrue(context.getOutputHistory().isEmpty());
        Assertions.assertFalse(context.getErrorHistory().isEmpty());
        String count = min + "" + (min == max ? "" : "-" +
            (max == Integer.MAX_VALUE ? '∞' : max)
        );
        Assertions.assertTrue(context.getErrorHistory().contains(
            "error: expected argument count: " + count
        ));
    }

    private void assertArgumentPresentInOutput(String argument) {
        Assertions.assertTrue(
            context.getOutputHistory().contains(argument),
            String.format("'%s' not present", argument)
        );
    }

    // endregion
    // region command

    private void executeCommand(Input input) {
        Command command = getCommand(input.getCommand());
        Assertions.assertNotNull(command, "command not found: " + input.getCommand());
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
