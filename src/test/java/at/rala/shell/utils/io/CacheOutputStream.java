package at.rala.shell.utils.io;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static at.rala.shell.utils.io.Converter.convertIntegerCollectionToString;

@SuppressWarnings("unused")
public class CacheOutputStream extends OutputStream {
    private static final int CARRIAGE_RETURN_CODE_POINT = 13;
    private final BlockingQueue<String> outputs = new LinkedBlockingQueue<>();
    private final List<Integer> cache = new ArrayList<>();

    @Override
    public void write(int b) {
        if (b == CARRIAGE_RETURN_CODE_POINT) return;
        if (b != '\n') cache.add(b);
        else if (cache.isEmpty()) outputs.add("");
        else {
            try {
                outputs.put(convertIntegerCollectionToString(cache));
            } catch (InterruptedException e) {
                return;
            }
            cache.clear();
        }
    }

    @Override
    public String toString() {
        return String.join(", ", getOutputs());
    }

    public BlockingQueue<String> getOutputs() {
        return outputs;
    }
}
