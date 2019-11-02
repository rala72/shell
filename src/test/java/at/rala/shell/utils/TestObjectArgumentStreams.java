package at.rala.shell.utils;

import at.rala.shell.Input;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestObjectArgumentStreams {
    private TestObjectArgumentStreams() {
    }

    static Stream<Arguments> getMethodParameterArguments() {
        return Stream.of(
            Arguments.of(new Input(
                "commandWithoutAttributesAndMethodWithoutParameter"
            ), 0),
            Arguments.of(new Input(
                "commandWithoutAttributesAndMethodWithoutParameter",
                "dummy"
            ), 0),
            Arguments.of(new Input(
                "commandWithoutAttributesAndMethodWithOneStringParameter"
            ), 1),
            Arguments.of(new Input(
                "commandWithoutAttributesAndMethodWithOneStringParameter",
                "dummy"
            ), 1),
            Arguments.of(new Input(
                "commandWithoutAttributesAndMethodWithOneStringParameter",
                "dummy", "dummy"
            ), 1),
            Arguments.of(new Input(
                "commandWithoutAttributesAndMethodWithTwoStringParameter"
            ), 2),
            Arguments.of(new Input(
                "commandWithoutAttributesAndMethodWithTwoStringParameter",
                "dummy"
            ), 2),
            Arguments.of(new Input(
                "commandWithoutAttributesAndMethodWithTwoStringParameter",
                "dummy", "dummy"
            ), 2),
            Arguments.of(new Input(
                "commandWithoutAttributesAndMethodWithTwoStringParameter",
                "dummy", "dummy", "dummy"
            ), 2)
        );
    }
}
