package io.rala.shell.annotation;

import io.rala.shell.exception.CommandAlreadyPresentException;
import io.rala.shell.exception.IllegalParameterException;
import io.rala.shell.testUtils.CommandLoaderFactory;
import io.rala.shell.testUtils.object.TestObjectWithOptional;
import io.rala.shell.testUtils.object.TestObjectWithOptionalDefaultValue;
import io.rala.shell.testUtils.object.TestObjectWithoutAttributes;
import io.rala.shell.testUtils.object.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandLoaderTest {
    @Test
    void privateObjectLoadingThrowsIllegalParameter() {
        try {
            new CommandLoader(new PrivateTestObject());
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("object has to be public", e.getMessage());
            return;
        }
        Assertions.fail();
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
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithOptional());
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
    }

    @Test
    void commandWithOneOptionalDefaultValueParameterLoading() {
        CommandLoader commandLoader = new CommandLoader(
            new TestObjectWithOptionalDefaultValue()
        );
        Assertions.assertEquals(1, commandLoader.getCommandMethodMap().size());
    }

    @Test
    void commandWithOneOptionalInvalidDefaultValueException() {
        try {
            new CommandLoader(new TestObjectWithOptionalInvalidDefaultValue());
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "arg1: default value is invalid",
                e.getMessage()
            );
            return;
        }
        Assertions.fail();
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
            new CommandLoader(new TestObjectWithTwoInputs());
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithTwoInputs: if input present, no other parameter allowed",
                e.getMessage()
            );
            return;
        }
        Assertions.fail();
    }

    @Test
    void commandWithTwoArrayParameterException() {
        try {
            new CommandLoader(new TestObjectWithTwoArrays());
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithTwoArrays: may only have one dynamic parameter",
                e.getMessage()
            );
            return;
        }
        Assertions.fail();
    }

    @Test
    void commandWithTwoListParameterException() {
        try {
            new CommandLoader(new TestObjectWithTwoLists());
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithTwoLists: may only have one dynamic parameter",
                e.getMessage()
            );
            return;
        }
        Assertions.fail();
    }

    @Test
    void commandWithOneArrayParameterNotOnEndException() {
        try {
            new CommandLoader(new TestObjectWithArrayNotOnEnd());
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithArrayNotOnEnd: only last parameter may be dynamic",
                e.getMessage()
            );
            return;
        }
        Assertions.fail();
    }

    @Test
    void commandWithOneOptionalParameterNotOnEndException() {
        try {
            new CommandLoader(new TestObjectWithOptionalNotOnEnd());
        } catch (IllegalParameterException e) {
            Assertions.assertEquals(
                "commandWithOptionalNotOnEnd: only last parameters can be absent",
                e.getMessage()
            );
            return;
        }
        Assertions.fail();
    }

    @Test
    void commandNotUniqueException() {
        try {
            new CommandLoader(new CommandNotUniqueErrorTestObject());
        } catch (CommandAlreadyPresentException e) {
            Assertions.assertEquals("cmd", e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void commandIllegalAccessException() {
        try {
            new CommandLoader(new IllegalAccessErrorTestObject());
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("object has no visible commands", e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void toStringOfObjectWithoutAttributes() {
        CommandLoader commandLoader = new CommandLoader(new TestObjectWithoutAttributes());
        Assertions.assertEquals("commandWithoutAttributes", commandLoader.toString());
    }

    private static class PrivateTestObject {
    }
}
