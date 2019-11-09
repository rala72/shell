package io.rala.shell.utils;

import io.rala.shell.testUtils.arguments.ParameterArgumentsStreamFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class StringMapperTest {
    @ParameterizedTest
    @MethodSource("getValidMappingArguments")
    void mapValidString(Class<?> type, String s) {
        StringMapper stringMapper = new StringMapper(s);
        Object map = stringMapper.map(type);
        Assertions.assertEquals(s, String.valueOf(map));
    }

    @ParameterizedTest
    @MethodSource("getInvalidMappingArguments")
    void mapInvalidString(Class<?> type, String s) {
        StringMapper stringMapper = new StringMapper(s);
        if (type.getSimpleName().equalsIgnoreCase("Boolean") && (type.isPrimitive() || !s.equals("null"))) {
            Object map = stringMapper.map(type);
            Assertions.assertEquals("false", String.valueOf(map));
        } else if ((!type.isPrimitive() || type.getName().equals("char")) && s.equals("null")) {
            Object map = stringMapper.map(type);
            Assertions.assertEquals(s, String.valueOf(map));
        } else {
            Assertions.assertThrows(NumberFormatException.class,
                () -> stringMapper.map(type)
            );
        }
    }

    @Test
    void toStringOfCommandWithoutAttributes() {
        Assertions.assertEquals("text", new StringMapper("text").toString());
    }

    // region arguments stream

    private static Stream<Arguments> getValidMappingArguments() {
        return ParameterArgumentsStreamFactory.validMapping();
    }

    private static Stream<Arguments> getInvalidMappingArguments() {
        return ParameterArgumentsStreamFactory.invalidMapping();
    }

    // endregion
}
