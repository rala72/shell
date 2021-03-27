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

import static org.junit.jupiter.api.Assertions.*;

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
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("object has to be public", e.getMessage());
        }
    }

    @Test
    void commandWithoutAttributesLoading() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithoutAttributes();
        assertEquals(1, commandLoader.getCommandMethodMap().size());
        assertTrue(commandLoader.getCommandMethodMap().containsKey("commandWithoutAttributes"));
    }

    @Test
    void commandWithAttributesLoading() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithAttributes();
        assertEquals(1, commandLoader.getCommandMethodMap().size());
        assertTrue(commandLoader.getCommandMethodMap().containsKey("cmd"));
    }

    @Test
    void commandWithOneOptionalParameterLoading() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithOptional(), stringMapper);
        assertEquals(1, commandLoader.getCommandMethodMap().size());
    }

    @Test
    void commandWithOneOptionalDefaultValueParameterLoading() {
        CommandLoader commandLoader = new CommandLoader(
            new TestObjectWithOptionalDefaultValue(),
            stringMapper);
        assertEquals(1, commandLoader.getCommandMethodMap().size());
    }

    @Test
    void commandWithOneOptionalInvalidDefaultValueException() {
        try {
            new CommandLoader(new TestObjectWithOptionalInvalidDefaultValue(), stringMapper);
            fail();
        } catch (IllegalParameterException e) {
            assertEquals(
                "arg1: default value is invalid",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithOneInputParameterException() {
        CommandLoader commandLoader = CommandLoaderFactory.getCommandLoaderForTestObjectWithOneInput();
        assertEquals(1, commandLoader.getCommandMethodMap().size());
        assertTrue(commandLoader.getCommandMethodMap().containsKey("commandWithOneInput"));
    }

    @Test
    void commandWithTwoInputParameterException() {
        try {
            new CommandLoader(new TestObjectWithTwoInputs(), stringMapper);
            fail();
        } catch (IllegalParameterException e) {
            assertEquals(
                "commandWithTwoInputs: if input present, no other parameter allowed",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithTwoArrayParameterException() {
        try {
            new CommandLoader(new TestObjectWithTwoArrays(), stringMapper);
            fail();
        } catch (IllegalParameterException e) {
            assertEquals(
                "commandWithTwoArrays: may only have one dynamic parameter",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithTwoListParameterException() {
        try {
            new CommandLoader(new TestObjectWithTwoLists(), stringMapper);
            fail();
        } catch (IllegalParameterException e) {
            assertEquals(
                "commandWithTwoLists: may only have one dynamic parameter",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithOneArrayParameterNotOnEndException() {
        try {
            new CommandLoader(new TestObjectWithArrayNotOnEnd(), stringMapper);
            fail();
        } catch (IllegalParameterException e) {
            assertEquals(
                "commandWithArrayNotOnEnd: only last parameter may be dynamic",
                e.getMessage()
            );
        }
    }

    @Test
    void commandWithOneOptionalParameterNotOnEndException() {
        try {
            new CommandLoader(new TestObjectWithOptionalNotOnEnd(), stringMapper);
            fail();
        } catch (IllegalParameterException e) {
            assertEquals(
                "commandWithOptionalNotOnEnd: only last parameters can be absent",
                e.getMessage()
            );
        }
    }

    @Test
    void commandNotUniqueException() {
        try {
            new CommandLoader(new CommandNotUniqueErrorTestObject(), stringMapper);
            fail();
        } catch (CommandAlreadyPresentException e) {
            assertEquals("cmd", e.getMessage());
        }
    }

    @Test
    void commandIllegalAccessException() {
        try {
            new CommandLoader(new IllegalAccessErrorTestObject(), stringMapper);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("object has no visible commands", e.getMessage());
        }
    }

    @Test
    void toStringOfObjectWithoutAttributes() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithoutAttributes(), stringMapper);
        assertEquals("commandWithoutAttributes", commandLoader.toString());
    }

    private static class PrivateTestObject {
    }
}
