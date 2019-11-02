package at.rala.shell.command;

import at.rala.shell.Input;
import at.rala.shell.annotation.CommandLoader;
import at.rala.shell.utils.TestContext;
import at.rala.shell.utils.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    // region commandWithoutAttributesAndMethodWithoutParameter

    @Test
    void testCommandWithoutAttributesAndMethodWithoutParameterWithoutParameter() {
        executeCommand(new Input(
            "commandWithoutAttributesAndMethodWithoutParameter"
        ));
        Assertions.assertTrue(context.getOutputHistory().isEmpty());
        Assertions.assertTrue(context.getErrorHistory().isEmpty());
    }

    @Test
    void testCommandWithoutAttributesAndMethodWithoutParameterWithParameter() {
        executeCommand(new Input(
            "commandWithoutAttributesAndMethodWithoutParameter",
            "dummy"
        ));
        Assertions.assertTrue(context.getOutputHistory().isEmpty());
        Assertions.assertFalse(context.getErrorHistory().isEmpty());
        Assertions.assertTrue(context.getErrorHistory().contains("error: expected argument count: 0"));
    }

    // endregion

    // region commandWithoutAttributesAndMethodWithOneStringParameter

    @Test
    void testCommandWithoutAttributesAndMethodWithOneStringParameterWithoutParameter() {
        executeCommand(new Input(
            "commandWithoutAttributesAndMethodWithOneStringParameter"
        ));
        Assertions.assertTrue(context.getOutputHistory().isEmpty());
        Assertions.assertFalse(context.getErrorHistory().isEmpty());
        Assertions.assertTrue(context.getErrorHistory().contains("error: expected argument count: 1"));
    }

    @Test
    void testCommandWithoutAttributesAndMethodWithOneStringParameterWithParameter() {
        executeCommand(new Input(
            "commandWithoutAttributesAndMethodWithOneStringParameter",
            "dummy"
        ));
        Assertions.assertTrue(context.getOutputHistory().isEmpty());
        Assertions.assertTrue(context.getErrorHistory().isEmpty());
    }

    // endregion

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

    private Command getCommand(String name) {
        return context.getCommands().get(name);
    }
}
