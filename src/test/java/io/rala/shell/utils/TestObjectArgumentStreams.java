package io.rala.shell.utils;

import io.rala.shell.Input;
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
            createMethodWithStringParameterArgumentsStream(
                "methodWithoutParameter", "none", 0, 2, 0
            ),
            createMethodWithStringParameterArgumentsStream(
                "methodWithOneStringParameter", "one", 0, 3, 1
            ),
            createMethodWithStringParameterArgumentsStream(
                "methodWithTwoStringParameter", "two", 1, 4, 2
            ),
            createMethodWithStringParameterArgumentsStream(
                "methodWithOneStringVarargsParameter", "vararg", 0, 4, null
            ),
            createMethodWithStringParameterArgumentsStream(
                "methodWithOneStringArrayParameter", "array", 0, 4, null
            )
        ).flatMap(argumentsStream -> argumentsStream);
    }

    public static Stream<Arguments> getMethodMappingParameterArguments() {
        return Stream.of(
            createMethodWithMappingParameterArgumentsStream(
                "Boolean",
                "false", "true"
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Byte",
                String.valueOf(Byte.MIN_VALUE), "0", String.valueOf(Byte.MAX_VALUE)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Char",
                "a", "z", "A", "Z", "\t", "#", "+"
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Short",
                String.valueOf(Short.MIN_VALUE), "0", String.valueOf(Short.MAX_VALUE)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Int",
                String.valueOf(Integer.MIN_VALUE), "0", String.valueOf(Integer.MAX_VALUE)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Long",
                String.valueOf(Long.MIN_VALUE), "0", String.valueOf(Long.MAX_VALUE)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Float",
                String.valueOf(Float.NEGATIVE_INFINITY),
                String.valueOf(-Float.MIN_VALUE),
                String.valueOf(Float.MIN_VALUE),
                String.valueOf(-Float.MIN_NORMAL),
                String.valueOf(Float.MIN_NORMAL),
                "0.0",
                String.valueOf(-Float.MAX_VALUE),
                String.valueOf(Float.MAX_VALUE),
                String.valueOf(Float.POSITIVE_INFINITY),
                String.valueOf(Float.NaN)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Double",
                String.valueOf(Double.NEGATIVE_INFINITY),
                String.valueOf(-Double.MIN_VALUE),
                String.valueOf(Double.MIN_VALUE),
                String.valueOf(-Double.MIN_NORMAL),
                String.valueOf(Double.MIN_NORMAL),
                "0.0", String.valueOf(Double.MAX_VALUE),
                String.valueOf(Double.POSITIVE_INFINITY),
                String.valueOf(Double.NaN)
            )
        ).flatMap(argumentsStream -> argumentsStream);
    }

    private static Stream<Arguments> createMethodWithStringParameterArgumentsStream(
        String name, String param, int from, int to, Integer expected
    ) {
        List<Arguments> list = new ArrayList<>();
        if (param != null)
            for (int i = from; i < to; i++)
                list.add(Arguments.of(new Input(name, Collections.nCopies(i, param)), expected));
        if (list.isEmpty()) list.add(Arguments.of(new Input(name), expected));
        return list.stream();
    }

    private static Stream<Arguments> createMethodWithMappingParameterArgumentsStream(
        String name, String... params
    ) {
        List<Arguments> list = new ArrayList<>();
        String[] types = new String[]{"Primitive", "Object"};
        for (String type : types) {
            String command = String.format("methodWithOne%s%sParameter", name, type);
            for (String param : params)
                list.add(Arguments.of(new Input(command, param)));
        }
        return list.stream();
    }
}
