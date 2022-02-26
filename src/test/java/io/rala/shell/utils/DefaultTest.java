package io.rala.shell.utils;

import io.rala.shell.testUtils.arguments.DefaultValueArgumentsStreamFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultTest {
    @ParameterizedTest
    @MethodSource("getDefaultValueArguments")
    <T> void defaultValue(Class<T> c, T defaultValue) {
        assertThat(Default.of(c)).isEqualTo(defaultValue);
    }

    private static Stream<Arguments> getDefaultValueArguments() {
        return DefaultValueArgumentsStreamFactory.defaultValues();
    }
}
