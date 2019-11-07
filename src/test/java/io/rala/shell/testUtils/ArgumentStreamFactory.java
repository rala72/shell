package io.rala.shell.testUtils;

import io.rala.shell.Input;
import org.junit.jupiter.params.provider.Arguments;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ArgumentStreamFactory {
    private ArgumentStreamFactory() {
    }

    public static Stream<Arguments> getMethodStringParameterArguments() {
        return Stream.of(
            createMethodWithStringParameterArgumentsStream(
                "methodWithOneInputParameter", "input", 0, 4, null
            ),
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
            ),
            createMethodWithStringParameterArgumentsStream(
                "methodWithOneStringListParameter", "list", 0, 4, null
            )
        ).flatMap(argumentsStream -> argumentsStream);
    }

    public static Stream<Arguments> getMethodValidMappingParameterArguments() {
        return Stream.of(
            createMethodWithMappingParameterArgumentsStream(
                "Boolean",
                ParameterFactory.validBooleanStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Byte",
                ParameterFactory.validByteStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Char",
                ParameterFactory.validCharStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Short",
                ParameterFactory.validShortStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Int",
                ParameterFactory.validIntegerStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Long",
                ParameterFactory.validLongStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Float",
                ParameterFactory.validFloatStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Double",
                ParameterFactory.validDoubleStream().toArray(String[]::new)
            )
        ).flatMap(argumentsStream -> argumentsStream);
    }

    public static Stream<Arguments> getMethodInvalidValidMappingParameterArguments() {
        return Stream.of(
            createMethodWithMappingParameterArgumentsStream(
                "Boolean",
                ParameterFactory.invalidBooleanStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Byte",
                ParameterFactory.invalidByteStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Char",
                ParameterFactory.invalidCharStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Short",
                ParameterFactory.invalidShortStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Int",
                ParameterFactory.invalidIntegerStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Long",
                ParameterFactory.invalidLongStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Float",
                ParameterFactory.invalidFloatStream().toArray(String[]::new)
            ),
            createMethodWithMappingParameterArgumentsStream(
                "Double",
                ParameterFactory.invalidDoubleStream().toArray(String[]::new)
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
                list.add(Arguments.of(
                    name, type.equals("Primitive"),
                    new Input(command, param))
                );
        }
        return list.stream();
    }
}
