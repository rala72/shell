package at.rala.shell.utils.io;

import java.util.Collection;
import java.util.stream.Collectors;

class Converter {
    private Converter() {
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
