package at.rala.shell.command;

import at.rala.shell.Input;
import at.rala.shell.annotation.CommandLoader;
import at.rala.shell.utils.TestContext;
import at.rala.shell.utils.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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
        Command command = getCommand(
            "commandWithoutAttributesAndMethodWithoutParameter"
        );
        Assertions.assertTrue(command.getDocumentation().isBlank());
    }

    @Test
    void testUsageOfCommandWithoutAttributes() {
        Command command = getCommand(
            "commandWithoutAttributesAndMethodWithoutParameter"
        );
        Assertions.assertTrue(command.getUsage().isBlank());
    }

    @Test
    void testDocumentationOfCommandWithAttributes() {
        Assertions.assertNull(getCommand(
            "commandWithAttributesAndMethodWithoutParameter"
        ));
        Command command = getCommand("value");
        Assertions.assertNotNull(command);
        Assertions.assertFalse(command.getDocumentation().isBlank());
        Assertions.assertEquals("documentation", command.getDocumentation());
    }

    @Test
    void testUsageOfCommandWithAttributes() {
        Assertions.assertNull(getCommand(
            "commandWithAttributesAndMethodWithoutParameter"
        ));
        Command command = getCommand("value");
        Assertions.assertNotNull(command);
        Assertions.assertFalse(command.getUsage().isBlank());
        Assertions.assertEquals("usage", command.getUsage());
    }

    // endregion

    @ParameterizedTest
    @MethodSource(
        "at.rala.shell.utils.TestObjectArgumentStreams#getMethodParameterArguments"
    )
    void testCommandWithoutAttributes(Input input, int expectedArguments) {
        executeCommand(input);
        if (input.getArguments().size() == expectedArguments) assertOutputsAreEmpty();
        else assertErrorOutputContainsExpectedArgumentCount(expectedArguments);
    }


    @Test
    void testToStringOfCommandWithoutAttributes() {
        Command command = getCommand("commandWithoutAttributesAndMethodWithoutParameter");
        String toString = "CommandMethodAdapter{object=TestObject, " +
            "commandMethod=CommandMethod{" +
            "command=Command(value=\"\", documentation=\"\"), " +
            "method=commandWithoutAttributesAndMethodWithoutParameter}}";
        Assertions.assertEquals(toString, command.toString());
    }

    private void executeCommand(Input input) {
        Command command = getCommand(input.getCommand());
        Assertions.assertNotNull(command);
        command.execute(input, context);
    }

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

    private Command getCommand(String name) {
        return context.getCommands().get(name);
    }
}
