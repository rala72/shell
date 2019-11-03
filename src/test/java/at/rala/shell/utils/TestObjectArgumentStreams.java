package at.rala.shell.utils;

import at.rala.shell.Input;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

@SuppressWarnings("unused")
public class TestObjectArgumentStreams {
    private TestObjectArgumentStreams() {
    }

    public static Stream<Arguments> getMethodStringParameterArguments() {
        return Stream.of(
            Arguments.of(new Input(
                "methodWithoutParameter"
            ), 0),
            Arguments.of(new Input(
                "methodWithoutParameter",
                "dummy"
            ), 0),
            Arguments.of(new Input(
                "methodWithOneStringParameter"
            ), 1),
            Arguments.of(new Input(
                "methodWithOneStringParameter",
                "dummy"
            ), 1),
            Arguments.of(new Input(
                "methodWithOneStringParameter",
                "dummy", "dummy"
            ), 1),
            Arguments.of(new Input(
                "methodWithTwoStringParameter"
            ), 2),
            Arguments.of(new Input(
                "methodWithTwoStringParameter",
                "dummy"
            ), 2),
            Arguments.of(new Input(
                "methodWithTwoStringParameter",
                "dummy", "dummy"
            ), 2),
            Arguments.of(new Input(
                "methodWithTwoStringParameter",
                "dummy", "dummy", "dummy"
            ), 2),
            Arguments.of(new Input(
                "methodWithOneStringVarargsParameter"
            ), null),
            Arguments.of(new Input(
                "methodWithOneStringVarargsParameter",
                "dummy"
            ), null),
            Arguments.of(new Input(
                "methodWithOneStringVarargsParameter",
                "dummy", "dummy"
            ), null),
            Arguments.of(new Input(
                "methodWithOneStringArrayParameter"
            ), null),
            Arguments.of(new Input(
                "methodWithOneStringArrayParameter",
                "dummy"
            ), null),
            Arguments.of(new Input(
                "methodWithOneStringArrayParameter",
                "dummy", "dummy"
            ), null)
        );
    }

    public static Stream<Arguments> getMethodMappingParameterArguments() {
        return Stream.of(
            Arguments.of(new Input(
                "methodWithOneBooleanPrimitiveParameter", "false"
            )),
            Arguments.of(new Input(
                "methodWithOneBooleanPrimitiveParameter", "true"
            )),
            Arguments.of(new Input(
                "methodWithOneBooleanObjectParameter", "false"
            )),
            Arguments.of(new Input(
                "methodWithOneBooleanObjectParameter", "true"
            )),
            Arguments.of(new Input(
                "methodWithOneBytePrimitiveParameter", "-128"
            )),
            Arguments.of(new Input(
                "methodWithOneBytePrimitiveParameter", "0"
            )),
            Arguments.of(new Input(
                "methodWithOneBytePrimitiveParameter", "127"
            )),
            Arguments.of(new Input(
                "methodWithOneByteObjectParameter", "-128"
            )),
            Arguments.of(new Input(
                "methodWithOneByteObjectParameter", "0"
            )),
            Arguments.of(new Input(
                "methodWithOneByteObjectParameter", "127"
            ))
        );
    }
}
