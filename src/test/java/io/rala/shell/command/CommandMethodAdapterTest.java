package io.rala.shell.command;

import io.rala.shell.Input;
import io.rala.shell.annotation.CommandAnnotation;
import io.rala.shell.annotation.CommandLoader;
import io.rala.shell.annotation.CommandMethod;
import io.rala.shell.exception.MethodCallException;
import io.rala.shell.utils.TestContext;
import io.rala.shell.utils.TestObject;
import io.rala.shell.utils.TestObjectArgumentStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    void testDocumentationOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        Assertions.assertTrue(command.getDocumentation().isBlank());
    }

    @Test
    void testUsageOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        Assertions.assertTrue(command.getUsage().isBlank());
    }

    @Test
    void testDocumentationOfCommandWithAttributes() {
        Assertions.assertNull(getCommand("commandWithAttributes"));
        Command command = getCommand("value");
        Assertions.assertNotNull(command);
        Assertions.assertFalse(command.getDocumentation().isBlank());
        Assertions.assertEquals("documentation", command.getDocumentation());
    }

    @Test
    void testUsageOfCommandWithAttributes() {
        Assertions.assertNull(getCommand("commandWithAttributes"));
        Command command = getCommand("value");
        Assertions.assertNotNull(command);
        Assertions.assertFalse(command.getUsage().isBlank());
        Assertions.assertEquals("usage", command.getUsage());
    }

    // endregion

    @ParameterizedTest
    @MethodSource("getStringArguments")
    void testCommandWithoutAttributes(Input input, Integer expectedArguments) {
        executeCommand(input);
        if (expectedArguments == null || input.getArguments().size() == expectedArguments)
            assertOutputsAreEmpty();
        else assertErrorOutputContainsExpectedArgumentCount(expectedArguments);
    }

    @ParameterizedTest
    @MethodSource("getValidMappingArguments")
    void testCommandWithValidParameterMapping(String name, boolean isPrimitive, Input input) {
        String command = input.getCommand();
        Assertions.assertTrue(command.contains(name));
        Assertions.assertTrue(command.contains(isPrimitive ? "Primitive" : "Object"));
        Assertions.assertEquals(1, input.getArguments().size(), "config error");

        executeCommand(input);

        String argument = input.getOrNull(0);
        Assertions.assertNotNull(argument);
        Assertions.assertTrue(context.getOutputHistory().contains(argument));
    }

    @ParameterizedTest
    @MethodSource("getInvalidMappingArguments")
    void testCommandWithInvalidParameterMapping(String name, boolean isPrimitive, Input input) {
        String command = input.getCommand();
        Assertions.assertTrue(command.contains(name));
        Assertions.assertTrue(command.contains(isPrimitive ? "Primitive" : "Object"));
        Assertions.assertEquals(1, input.getArguments().size(), "config error");

        String argument = input.getOrNull(0);
        Assertions.assertNotNull(argument);
        if (name.equals("Boolean") && (isPrimitive || !argument.equals("null"))) {
            executeCommand(input);
            Assertions.assertTrue(context.getOutputHistory().contains("false"));
        } else if (!isPrimitive && argument.equals("null")) {
            executeCommand(input);
            Assertions.assertTrue(context.getOutputHistory().contains(argument));
        } else {
            Assertions.assertThrows(MethodCallException.class,
                () -> executeCommand(input)
            );
        }
    }

    // region execute exception

    @Test
    void testExceptionCommandWithoutAttributes() {
        Assertions.assertThrows(MethodCallException.class,
            () -> executeCommand(new Input("exceptionCommandWithoutMessage"))
        );
    }

    @Test
    void testIllegalAccessCommandWithoutAttributes() throws NoSuchMethodException {
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
    void testToStringOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        String toString = "CommandMethodAdapter{object=TestObject, " +
            "commandMethod=CommandMethod{" +
            "command=Command(value=\"\", documentation=\"\"), " +
            "method=methodWithoutParameter}}";
        Assertions.assertEquals(toString, command.toString());
    }

    // region assert

    private void assertOutputsAreEmpty() {
        Assertions.assertTrue(context.getOutputHistory().isEmpty());
        Assertions.assertTrue(context.getErrorHistory().isEmpty());
    }

    private void assertErrorOutputContainsExpectedArgumentCount(int count) {
        Assertions.assertTrue(context.getOutputHistory().isEmpty());
        Assertions.assertFalse(context.getErrorHistory().isEmpty());
        Assertions.assertTrue(context.getErrorHistory().contains(
            "error: expected argument count: " + count
        ));
    }

    // endregion
    // region command

    private void executeCommand(Input input) {
        Command command = getCommand(input.getCommand());
        Assertions.assertNotNull(command);
        command.execute(input, context);
    }

    private Command getCommand(String name) {
        return context.getCommands().get(name);
    }

    // endregion
    // region arguments stream

    private static Stream<Arguments> getStringArguments() {
        return TestObjectArgumentStreams.getMethodStringParameterArguments();
    }

    private static Stream<Arguments> getValidMappingArguments() {
        return TestObjectArgumentStreams.getMethodValidMappingParameterArguments();
    }

    private static Stream<Arguments> getInvalidMappingArguments() {
        return TestObjectArgumentStreams.getMethodInvalidValidMappingParameterArguments();
    }

    // endregion
}
