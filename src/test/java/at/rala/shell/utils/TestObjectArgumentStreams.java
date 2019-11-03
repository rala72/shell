package at.rala.shell.utils;

import at.rala.shell.Input;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class TestObjectArgumentStreams {
    private TestObjectArgumentStreams() {
    }

    public static Stream<Arguments> getMethodStringParameterArguments() {
        return Stream.of(
            getMethodWithStringParameterArguments(
                "methodWithoutParameter", "none", 0, 2, 0
            ),
            getMethodWithStringParameterArguments(
                "methodWithOneStringParameter", "one", 0, 3, 1
            ),
            getMethodWithStringParameterArguments(
                "methodWithTwoStringParameter", "two", 1, 4, 2
            ),
            getMethodWithStringParameterArguments(
                "methodWithOneStringVarargsParameter", "vararg", 0, 4, null
            ),
            getMethodWithStringParameterArguments(
                "methodWithOneStringArrayParameter", "array", 0, 4, null
            )
        ).flatMap(argumentsStream -> argumentsStream);
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

    private static Stream<Arguments> getMethodWithStringParameterArguments(
        String name, String param, int from, int to, Integer expected
    ) {
        List<Arguments> list = new ArrayList<>();
        if (param != null)
            for (int i = from; i < to; i++)
                list.add(Arguments.of(new Input(name, Collections.nCopies(i, param)), expected));
        if (list.isEmpty()) list.add(Arguments.of(new Input(name), expected));
        return list.stream();
    }
}
