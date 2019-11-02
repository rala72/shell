package at.rala.shell.command;

import at.rala.shell.annotation.CommandLoader;
import at.rala.shell.utils.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandMethodAdapterTest {
    private CommandLoader commandLoader;

    @BeforeEach
    void setUp() {
        commandLoader = new CommandLoader(new TestObject());
    }

    @Test
    void testDocumentationOfCommandWithoutAttributes() {
        Command command = getCommand("commandWithoutAttributesAndMethodWithoutParameter");
        Assertions.assertTrue(command.getDocumentation().isBlank());
    }

    @Test
    void testUsageOfCommandWithoutAttributes() {
        Command command = getCommand("commandWithoutAttributesAndMethodWithoutParameter");
        Assertions.assertTrue(command.getUsage().isBlank());
    }

    @Test
    void testToStringOfWithoutAttributes() {
        Command command = getCommand("commandWithoutAttributesAndMethodWithoutParameter");
        String toString = "CommandMethodAdapter{object=TestObject, " +
            "commandMethod=CommandMethod{" +
            "command=Command(value=\"\", documentation=\"\"), " +
            "method=commandWithoutAttributesAndMethodWithoutParameter}}";
        Assertions.assertEquals(toString, command.toString());
    }

    private Command getCommand(String name) {
        return commandLoader.getCommandMethodMap().get(name);
    }
}
