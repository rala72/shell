package at.rala.shell.command;

import at.rala.shell.Input;
import at.rala.shell.annotation.CommandAnnotation;
import at.rala.shell.annotation.CommandLoader;
import at.rala.shell.annotation.CommandMethod;
import at.rala.shell.exception.MethodCallException;
import at.rala.shell.utils.TestContext;
import at.rala.shell.utils.TestObject;
import at.rala.shell.utils.TestObjectArgumentStreams;
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
        CommandLoader commandLoader = new CommandLoader(new TestObject());
        context = TestContext.getInstanceWithDifferentStreams(commandLoader.getCommandMethodMap());
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
    @MethodSource("getTestCommandWithoutAttributesArguments")
    void testCommandWithoutAttributes(Input input, Integer expectedArguments) {
        executeCommand(input);
        if (expectedArguments == null || input.getArguments().size() == expectedArguments)
            assertOutputsAreEmpty();
        else assertErrorOutputContainsExpectedArgumentCount(expectedArguments);
    }

    // region execute exception

    @Test
    void testExceptionCommandWithoutAttributes() {
        Assertions.assertThrows(MethodCallException.class,
            () -> executeCommand(new Input("exceptionCommand"))
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

    private static Stream<Arguments> getTestCommandWithoutAttributesArguments() {
        return TestObjectArgumentStreams.getMethodParameterArguments();
    }

    // endregion
}
