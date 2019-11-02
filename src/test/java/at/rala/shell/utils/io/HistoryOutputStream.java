package at.rala.shell.utils.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static at.rala.shell.utils.io.Converter.convertIntegerCollectionToString;

@SuppressWarnings({"unused", "WeakerAccess"})
public class HistoryOutputStream extends OutputStream {
    private static final int CARRIAGE_RETURN_CODE_POINT = 13;
    private final OutputStream passThrough;
    private final BlockingQueue<String> history = new LinkedBlockingQueue<>();
    private final List<Integer> current = new ArrayList<>();

    public HistoryOutputStream() {
        this(null);
    }

    public HistoryOutputStream(OutputStream passThrough) {
        this.passThrough = passThrough;
    }

    @Override
    public void write(int b) throws IOException {
        if (passThrough != null) passThrough.write(b);
        if (b == CARRIAGE_RETURN_CODE_POINT) return;
        if (b != '\n') current.add(b);
        else if (current.isEmpty()) history.add("");
        else {
            try {
                history.put(convertIntegerCollectionToString(current));
            } catch (InterruptedException e) {
                return;
            }
            current.clear();
        }
    }

    @Override
    public String toString() {
        return String.join(", ", getHistory());
    }

    public BlockingQueue<String> getHistory() {
        return history;
    }
}
