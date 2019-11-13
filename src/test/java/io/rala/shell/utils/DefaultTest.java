package io.rala.shell.utils;

import io.rala.shell.testUtils.arguments.DefaultValueArgumentsStreamFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class DefaultTest {
    @ParameterizedTest
    @MethodSource("getDefaultValueArguments")
    <T> void defaultValue(Class<T> c, T defaultValue) {
        Assertions.assertEquals(defaultValue, Default.of(c));
    }

    private static Stream<Arguments> getDefaultValueArguments() {
        return DefaultValueArgumentsStreamFactory.defaultValues();
    }
}
