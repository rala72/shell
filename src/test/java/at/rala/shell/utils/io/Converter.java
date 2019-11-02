package at.rala.shell.utils.io;

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
        return new String(
            collection.stream().mapToInt(Integer::intValue)
                .mapToObj(c -> Character.toString((char) c))
                .collect(Collectors.joining())
                .toCharArray()
        );
    }
}
