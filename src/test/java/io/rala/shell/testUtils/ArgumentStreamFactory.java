package io.rala.shell.testUtils;

import io.rala.shell.Input;
import org.junit.jupiter.params.provider.Arguments;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ArgumentStreamFactory {
    private ArgumentStreamFactory() {
    }

    public static Stream<Arguments> getValidMappingParameterArguments() {
        return Arrays.stream(PrimitiveParameterFactory.TYPE.values())
            .map(PrimitiveParameterFactory::validStreamHolderOf)
            .map(streamHolder -> createMappingParameterArgumentsStream(
                streamHolder.getClassName(),
                streamHolder.getStream().toArray(String[]::new)
            )).flatMap(argumentsStream -> argumentsStream);
    }

    public static Stream<Arguments> getInvalidMappingParameterArguments() {
        return Arrays.stream(PrimitiveParameterFactory.TYPE.values())
            .map(PrimitiveParameterFactory::invalidStreamHolderOf)
            .map(streamHolder -> createMappingParameterArgumentsStream(
                streamHolder.getClassName(),
                streamHolder.getStream().toArray(String[]::new)
            )).flatMap(argumentsStream -> argumentsStream);
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
        return Arrays.stream(PrimitiveParameterFactory.TYPE.values())
            .map(PrimitiveParameterFactory::validStreamHolderOf)
            .map(streamHolder -> createMethodWithMappingParameterArgumentsStream(
                streamHolder.getClassName(),
                streamHolder.getStream().toArray(String[]::new)
            )).flatMap(argumentsStream -> argumentsStream);
    }

    public static Stream<Arguments> getMethodInvalidValidMappingParameterArguments() {
        return Arrays.stream(PrimitiveParameterFactory.TYPE.values())
            .map(PrimitiveParameterFactory::invalidStreamHolderOf)
            .map(streamHolder -> createMethodWithMappingParameterArgumentsStream(
                streamHolder.getClassName(),
                streamHolder.getStream().toArray(String[]::new)
            )).flatMap(argumentsStream -> argumentsStream);
    }

    private static Stream<Arguments> createMappingParameterArgumentsStream(
        String name, String... strings
    ) {
        Class<?> type;
        try {
            type = Class.forName(String.format("java.lang.%s", name));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Stream.empty();
        }
        List<Arguments> list = new ArrayList<>();
        for (String param : strings) {
            list.add(Arguments.of(
                type, param
            ));
            Class<?> primitiveClass;
            try {
                Field primitiveField = type.getField("TYPE");
                primitiveClass = (Class<?>) primitiveField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                continue;
            }
            list.add(Arguments.of(primitiveClass, param));
        }
        return list.stream();
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
