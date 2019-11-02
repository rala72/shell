package at.rala.shell.utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class BlockingQueueInputStream extends InputStream {
    private final static int TIMEOUT = 5;
    private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private final BlockingQueue<String> queue;
    private StringReader stringReader;

    public BlockingQueueInputStream(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public int read() throws IOException {
        int read = stringReader != null ? stringReader.read() : -1;
        if (read == -1) {
            try {
                String remove = queue.poll(TIMEOUT, TIME_UNIT);
                if (remove == null) return -1;
                stringReader = new StringReader(remove);
                read = stringReader.read();
            } catch (InterruptedException ignored) {
            }
        }
        return read;
    }

    @Override
    public String toString() {
        return String.join(", ", queue);
    }
}
