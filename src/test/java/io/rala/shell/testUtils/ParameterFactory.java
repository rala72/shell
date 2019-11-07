package io.rala.shell.testUtils;

import java.util.Arrays;
import java.util.stream.Stream;

class ParameterFactory {
    @SuppressWarnings("SpellCheckingInspection")
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String SPECIAL_LETTERS = "äöüß";

    private ParameterFactory() {
    }

    // region valid

    static Stream<String> validBooleanStream() {
        return Stream.of(
            "false", "true"
        );
    }

    static Stream<String> validByteStream() {
        return Stream.of(
            String.valueOf(Byte.MIN_VALUE), "0", String.valueOf(Byte.MAX_VALUE)
        );
    }

    static Stream<String> validCharStream() {
        return Stream.of(
            ALPHABET.toLowerCase().split(""),
            ALPHABET.toUpperCase().split(""),
            SPECIAL_LETTERS.toLowerCase().split(""),
            SPECIAL_LETTERS.toUpperCase().split(""),
            new String[]{"\t", "\\", "\"", "#", "+", "*", "?"}
        ).flatMap(Arrays::stream);
    }

    static Stream<String> validShortStream() {
        return Stream.of(
            String.valueOf(Short.MIN_VALUE), "0", String.valueOf(Short.MAX_VALUE)
        );
    }

    static Stream<String> validIntegerStream() {
        return Stream.of(
            String.valueOf(Integer.MIN_VALUE), "0", String.valueOf(Integer.MAX_VALUE)
        );
    }

    static Stream<String> validLongStream() {
        return Stream.of(
            String.valueOf(Long.MIN_VALUE), "0", String.valueOf(Long.MAX_VALUE)
        );
    }

    static Stream<String> validFloatStream() {
        return Stream.of(
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
        );
    }

    static Stream<String> validDoubleStream() {
        return Stream.of(
            String.valueOf(Double.NEGATIVE_INFINITY),
            String.valueOf(-Double.MIN_VALUE),
            String.valueOf(Double.MIN_VALUE),
            String.valueOf(-Double.MIN_NORMAL),
            String.valueOf(Double.MIN_NORMAL),
            "0.0", String.valueOf(Double.MAX_VALUE),
            String.valueOf(Double.POSITIVE_INFINITY),
            String.valueOf(Double.NaN)
        );
    }

    // endregion
    // region invalid

    static Stream<String> invalidBooleanStream() {
        return Stream.of(
            "null", "-1"
        );
    }

    static Stream<String> invalidByteStream() {
        return Stream.of(
            "null",
            String.valueOf(Byte.MIN_VALUE - 1),
            String.valueOf(Byte.MAX_VALUE + 1)
        );
    }

    static Stream<String> invalidCharStream() {
        return Stream.of(
            "null"
        );
    }

    static Stream<String> invalidShortStream() {
        return Stream.of(
            "null",
            String.valueOf(Short.MIN_VALUE - 1),
            String.valueOf(Short.MAX_VALUE + 1)
        );
    }

    static Stream<String> invalidIntegerStream() {
        return Stream.of(
            "null",
            String.valueOf(Integer.MIN_VALUE - 1L),
            String.valueOf(Integer.MAX_VALUE + 1L)
        );
    }

    static Stream<String> invalidLongStream() {
        return Stream.of(
            "null"
        );
    }

    static Stream<String> invalidFloatStream() {
        return Stream.of(
            "null"
        );
    }

    static Stream<String> invalidDoubleStream() {
        return Stream.of(
            "null"
        );
    }

    // endregion
}
