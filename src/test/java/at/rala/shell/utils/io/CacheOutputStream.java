package at.rala.shell.utils.io;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CacheOutputStream extends OutputStream {
    private static final int FLUSH_CODE_POINT = 13;
    private final List<String> outputs = new ArrayList<>();
    private final List<Integer> cache = new ArrayList<>();

    @Override
    public void write(int b) {
        if (b == FLUSH_CODE_POINT) return;
        if (b != '\n') cache.add(b);
        else if (cache.isEmpty()) outputs.add("");
        else {
            outputs.add(convertIntegerCollectionToString(cache));
            cache.clear();
        }
    }

    @Override
    public String toString() {
        return String.join(", ", getOutputs());
    }

    public List<String> getOutputs() {
        return outputs;
    }

    private static String convertIntegerCollectionToString(Collection<Integer> collection) {
        return new String(
            collection.stream().mapToInt(Integer::intValue)
                .mapToObj(c -> Character.toString((char) c))
                .collect(Collectors.joining())
                .toCharArray()
        );
    }
}
