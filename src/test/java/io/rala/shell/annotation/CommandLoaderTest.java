package io.rala.shell.annotation;

import io.rala.StringMapper;
import io.rala.shell.exception.CommandAlreadyPresentException;
import io.rala.shell.exception.IllegalParameterException;
import io.rala.shell.testUtils.CommandLoaderFactory;
import io.rala.shell.testUtils.object.TestObjectWithOptional;
import io.rala.shell.testUtils.object.TestObjectWithOptionalDefaultValue;
import io.rala.shell.testUtils.object.TestObjectWithoutAttributes;
import io.rala.shell.testUtils.object.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandLoaderTest {
    private StringMapper stringMapper;

    @BeforeEach
    void setUp() {
        stringMapper = new StringMapper();
    }

    @Test
    void privateObjectLoadingThrowsIllegalParameter() {
        assertThatThrownBy(() -> new CommandLoader(new PrivateTestObject(), stringMapper))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("object has to be public");
    }

    @Test
    void commandWithoutAttributesLoading() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithoutAttributes();
        assertThat(commandLoader.getCommandMethodMap())
            .hasSize(1).containsKey("commandWithoutAttributes");
    }

    @Test
    void commandWithAttributesLoading() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithAttributes();
        assertThat(commandLoader.getCommandMethodMap())
            .hasSize(1).containsKey("cmd");
    }

    @Test
    void commandWithOneOptionalParameterLoading() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithOptional(), stringMapper);
        assertThat(commandLoader.getCommandMethodMap()).hasSize(1);
    }

    @Test
    void commandWithOneOptionalDefaultValueParameterLoading() {
        CommandLoader commandLoader = new CommandLoader(
            new TestObjectWithOptionalDefaultValue(),
            stringMapper);
        assertThat(commandLoader.getCommandMethodMap()).hasSize(1);
    }

    @Test
    void commandWithOneOptionalInvalidDefaultValueException() {
        assertThatThrownBy(() ->
            new CommandLoader(new TestObjectWithOptionalInvalidDefaultValue(), stringMapper)
        ).isInstanceOf(IllegalParameterException.class)
            .hasMessage("arg1: default value is invalid");
    }

    @Test
    void commandWithOneInputParameterException() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithOneInput();
        assertThat(commandLoader.getCommandMethodMap())
            .hasSize(1).containsKey("commandWithOneInput");
    }

    @Test
    void commandWithTwoInputParameterException() {
        assertThatThrownBy(() ->
            new CommandLoader(new TestObjectWithTwoInputs(), stringMapper)
        ).isInstanceOf(IllegalParameterException.class)
            .hasMessage("commandWithTwoInputs: if input present, no other parameter allowed");
    }

    @Test
    void commandWithTwoArrayParameterException() {
        assertThatThrownBy(() ->
            new CommandLoader(new TestObjectWithTwoArrays(), stringMapper)
        ).isInstanceOf(IllegalParameterException.class)
            .hasMessage("commandWithTwoArrays: may only have one dynamic parameter");
    }

    @Test
    void commandWithTwoListParameterException() {
        assertThatThrownBy(() ->
            new CommandLoader(new TestObjectWithTwoLists(), stringMapper)
        ).isInstanceOf(IllegalParameterException.class)
            .hasMessage("commandWithTwoLists: may only have one dynamic parameter");
    }

    @Test
    void commandWithOneArrayParameterNotOnEndException() {
        assertThatThrownBy(() ->
            new CommandLoader(new TestObjectWithArrayNotOnEnd(), stringMapper)
        ).isInstanceOf(IllegalParameterException.class)
            .hasMessage("commandWithArrayNotOnEnd: only last parameter may be dynamic");
    }

    @Test
    void commandWithOneOptionalParameterNotOnEndException() {
        assertThatThrownBy(() ->
            new CommandLoader(new TestObjectWithOptionalNotOnEnd(), stringMapper)
        ).isInstanceOf(IllegalParameterException.class)
            .hasMessage("commandWithOptionalNotOnEnd: only last parameters can be absent");
    }

    @Test
    void commandNotUniqueException() {
        assertThatThrownBy(() ->
            new CommandLoader(new CommandNotUniqueErrorTestObject(), stringMapper)
        ).isInstanceOf(CommandAlreadyPresentException.class)
            .hasMessage("cmd");
    }

    @Test
    void commandIllegalAccessException() {
        assertThatThrownBy(() ->
            new CommandLoader(new IllegalAccessErrorTestObject(), stringMapper)
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("object has no visible commands");
    }

    @Test
    void toStringOfObjectWithoutAttributes() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithoutAttributes(), stringMapper);
        assertThat(commandLoader).hasToString("commandWithoutAttributes");
    }

    private static class PrivateTestObject {
    }
}
