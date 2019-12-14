package io.rala.shell.testUtils.arguments;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class DefaultValueArgumentsStreamFactory {
    private DefaultValueArgumentsStreamFactory() {
    }

    public static Stream<Arguments> defaultValues() {
        return Stream.of(
            objectDefaultValues(),
            primitiveDefaultValues()
        ).flatMap(argumentsStream -> argumentsStream);
    }

    public static Stream<Arguments> objectDefaultValues() {
        return Stream.of(
            Arguments.of(Boolean.class, null),
            Arguments.of(Byte.class, null),
            Arguments.of(Character.class, null),
            Arguments.of(Short.class, null),
            Arguments.of(Integer.class, null),
            Arguments.of(Long.class, null),
            Arguments.of(Float.class, null),
            Arguments.of(Double.class, null)
        );
    }

    public static Stream<Arguments> primitiveDefaultValues() {
        return Stream.of(
            Arguments.of(Boolean.TYPE, false),
            Arguments.of(Byte.TYPE, (byte) 0),
            Arguments.of(Character.TYPE, '\0'),
            Arguments.of(Short.TYPE, (short) 0),
            Arguments.of(Integer.TYPE, 0),
            Arguments.of(Long.TYPE, 0L),
            Arguments.of(Float.TYPE, 0F),
            Arguments.of(Double.TYPE, 0D)
        );
    }
}
