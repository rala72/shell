package io.rala.shell.testUtils.io;

import java.util.Collection;
import java.util.stream.Collectors;

class Converter {
    private Converter() {
    }

    static char[] convertByteArrayToCharArray(byte[] array) {
        char[] converted = new char[array.length];
        for (int i = 0; i < array.length; i++) converted[i] = (char) array[i];
        return converted;
    }

    static byte[] convertCharArrayToByteArray(char[] array) {
        byte[] converted = new byte[array.length];
        for (int i = 0; i < array.length; i++) converted[i] = (byte) array[i];
        return converted;
    }

    static String convertIntegerCollectionToString(Collection<Integer> collection) {
        return collection.parallelStream()
            .map(integer -> String.valueOf((char) (int) integer))
            .collect(Collectors.joining());
    }
}
