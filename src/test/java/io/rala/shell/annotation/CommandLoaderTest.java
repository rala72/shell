package io.rala.shell.annotation;

import io.rala.StringMapper;
import io.rala.shell.exception.CommandAlreadyPresentException;
import io.rala.shell.exception.IllegalParameterException;
import io.rala.shell.testUtils.CommandLoaderFactory;
import io.rala.shell.testUtils.object.TestObjectWithOptional;
import io.rala.shell.testUtils.object.TestObjectWithOptionalDefaultValue;
import io.rala.shell.testUtils.object.TestObjectWithoutAttributes;
import io.rala.shell.testUtils.object.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandLoaderTest {
    private StringMapper stringMapper;

    @BeforeEach
    void setUp() {
        stringMapper = new StringMapper();
    }

    @Test
    void privateObjectLoadingThrowsIllegalParameter() {
        try {
            new CommandLoader(new PrivateTestObject(), stringMapper);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("object has to be public", e.getMessage());
        }
    }

    @Test
    void commandWithoutAttributesLoading() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithoutAttributes();
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("commandWithoutAttributes"));
    }

    @Test
    void commandWithAttributesLoading() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithAttributes();
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("cmd"));
    }

    @Test
    void commandWithOneOptionalParameterLoading() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithOptional(), stringMapper);
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
    }

    @Test
    void commandWithOneOptionalDefaultValueParameterLoading() {
        CommandLoader commandLoader = new CommandLoader(
            new TestObjectWithOptionalDefaultValue(),
            stringMapper);
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
    }

    @Test
    void commandWithOneOptionalInvalidDefaultValueException() {
        try {
            new CommandLoader(new TestObjectWithOptionalInvalidDefaultValue(), stringMapper);
            Assertions.fail();
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "arg1: default value is invalid",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithOneInputParameterException() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithOneInput();
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
        Assertions.assertTrue(commandLoader.getCommandMethodMap().containsKey("commandWithOneInput"));
    }

    @Test
    void commandWithTwoInputParameterException() {
        try {
            new CommandLoader(new TestObjectWithTwoInputs(), stringMapper);
            Assertions.fail();
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithTwoInputs: if input present, no other parameter allowed",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithTwoArrayParameterException() {
        try {
            new CommandLoader(new TestObjectWithTwoArrays(), stringMapper);
            Assertions.fail();
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithTwoArrays: may only have one dynamic parameter",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithTwoListParameterException() {
        try {
            new CommandLoader(new TestObjectWithTwoLists(), stringMapper);
            Assertions.fail();
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithTwoLists: may only have one dynamic parameter",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithOneArrayParameterNotOnEndException() {
        try {
            new CommandLoader(new TestObjectWithArrayNotOnEnd(), stringMapper);
            Assertions.fail();
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithArrayNotOnEnd: only last parameter may be dynamic",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithOneOptionalParameterNotOnEndException() {
        try {
            new CommandLoader(new TestObjectWithOptionalNotOnEnd(), stringMapper);
            Assertions.fail();
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithOptionalNotOnEnd: only last parameters can be absent",
                e.getMessage()
            );
        }
    }

    @Test
    void commandNotUniqueException() {
        try {
            new CommandLoader(new CommandNotUniqueErrorTestObject(), stringMapper);
            Assertions.fail();
        } catch (CommandAlreadyPresentException e) {
            Assertions.assertEquals("cmd", e.getMessage());
        }
    }

    @Test
    void commandIllegalAccessException() {
        try {
            new CommandLoader(new IllegalAccessErrorTestObject(), stringMapper);
            Assertions.fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("object has no visible commands", e.getMessage());
        }
    }

    @Test
    void toStringOfObjectWithoutAttributes() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithoutAttributes(), stringMapper);
        Assertions.assertEquals("commandWithoutAttributes", commandLoader.toString());
    }

    private static class PrivateTestObject {
    }
}
