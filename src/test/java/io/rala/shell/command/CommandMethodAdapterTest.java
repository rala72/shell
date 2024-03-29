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

import static org.assertj.core.api.Assertions.*;

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
        assertThat(command.getDocumentation())
            .isNotNull().isBlank();
    }

    @Test
    void usageOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        assertThat(command.getUsage())
            .isNotNull().isBlank();
    }

    @Test
    void documentationOfCommandWithAttributes() {
        assertThat(getCommand("commandWithAttributes")).isNull();
        Command command = getCommand("value");
        assertThat(command).isNotNull();
        assertThat(command.getDocumentation())
            .isNotBlank().isEqualTo("documentation");
    }

    @Test
    void usageOfCommandWithAttributes() {
        assertThat(getCommand("commandWithAttributes")).isNull();
        Command command = getCommand("value");
        assertThat(command).isNotNull();
        assertThat(command.getUsage())
            .isNotBlank().isEqualTo("usage");
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
        assertThat(command)
            .isNotNull()
            .contains(name)
            .contains(isPrimitive ? "Primitive" : "Object");
        assertThat(input.getArguments().size()).as("config error").isOne();

        executeCommand(input);

        String argument = input.getOrNull(0);
        assertThat(argument).isNotNull();
        assertArgumentPresentInOutput(argument);
    }

    @ParameterizedTest
    @MethodSource("getInvalidMappingArguments")
    void commandWithInvalidParameterMapping(String name, boolean isPrimitive, Input input) {
        String command = input.getCommand();
        assertThat(command)
            .isNotNull()
            .contains(name)
            .contains(isPrimitive ? "Primitive" : "Object");
        assertThat(input.getArguments()).as("config error").hasSize(1);

        String argument = input.getOrNull(0);
        assertThat(argument).isNotNull();
        if (name.equals("Boolean") && (isPrimitive || !argument.equals("null"))) {
            executeCommand(input);
            assertArgumentPresentInOutput("false");
        } else if (!isPrimitive && argument.equals("null")) {
            executeCommand(input);
            assertArgumentPresentInOutput(argument);
        } else {
            assertThatExceptionOfType(MethodCallException.class).isThrownBy(() -> executeCommand(input));
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
        assertThatThrownBy(() ->
            executeCommand(new Input("methodWithOneStringListParameter", "null"))
        ).isInstanceOf(MethodCallException.class)
            .hasMessage("java.lang.NullPointerException");
    }

    @Test
    void exceptionCommandWithoutAttributes() {
        assertThatThrownBy(() ->
            executeCommand(new Input("exceptionCommandWithoutMessage"))
        ).isInstanceOf(MethodCallException.class)
            .getCause()
            .isInstanceOf(InvocationTargetException.class)
            .hasMessage(null);
    }

    @Test
    void illegalAccessCommandWithoutAttributes() throws NoSuchMethodException {
        CommandMethodAdapter illegalAccessCommand = new CommandMethodAdapter(
            new TestObject(),
            new CommandMethod(new CommandAnnotation(),
                TestObject.class.getDeclaredMethod("illegalAccessCommand")
            )
        );
        assertThatExceptionOfType(MethodCallException.class)
            .isThrownBy(() -> illegalAccessCommand.execute(
                new Input("illegalAccessCommand")
                , context
            ));
    }

    // endregion

    @Test
    void toStringOfCommandWithoutAttributes() {
        Command command = getCommand("methodWithoutParameter");
        String toString = "CommandMethodAdapter{object=TestObject, " +
            "commandMethod=CommandMethod{" +
            "command=Command(value=\"\", documentation=\"\"), " +
            "method=methodWithoutParameter}}";
        assertThat(command).hasToString(toString);
    }

    // region assert

    private void assertOutputsAreEmpty() {
        assertThat(context.getOutputHistory()).as("output history is not empty").isEmpty();
        assertThat(context.getErrorHistory()).as("error history is not empty").isEmpty();
    }

    private void assertErrorOutputContainsExpectedArgumentCount(int min, int max) {
        assertThat(context.getOutputHistory()).isEmpty();
        assertThat(context.getErrorHistory()).isNotEmpty();
        String count = min + "" + (min == max ? "" : "-" +
            (max == Integer.MAX_VALUE ? '∞' : max)
        );
        assertThat(context.getErrorHistory()).contains("error: expected argument count: " + count);
    }

    private void assertArgumentPresentInOutput(String argument) {
        assertThat(context.getOutputHistory())
            .as(String.format("'%s' not present", argument)).contains(argument);
    }

    // endregion
    // region command

    private void executeCommand(Input input) {
        Command command = getCommand(input.getCommand());
        assertThat(command).as("command not found: " + input.getCommand()).isNotNull();
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
