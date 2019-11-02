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
    void testDocumentationOfSimpleCommand() {
        Command simpleCommandWithoutParameter = getCommand("simpleCommandWithoutParameter");
        Assertions.assertTrue(simpleCommandWithoutParameter.getDocumentation().isBlank());
    }

    @Test
    void testUsageOfSimpleCommand() {
        Command simpleCommandWithoutParameter = getCommand("simpleCommandWithoutParameter");
        Assertions.assertTrue(simpleCommandWithoutParameter.getUsage().isBlank());
    }

    @Test
    void testToStringOfSimpleCommand() {
        Command simpleCommandWithoutParameter = getCommand("simpleCommandWithoutParameter");
        String toString = "CommandMethodAdapter{object=TestObject, " +
            "commandMethod=CommandMethod{" +
            "command=Command(value=\"\", documentation=\"\"), " +
            "method=simpleCommandWithoutParameter}}";
        Assertions.assertEquals(toString, simpleCommandWithoutParameter.toString());
    }

    private Command getCommand(String name) {
        return commandLoader.getCommandMethodMap().get(name);
    }
}
