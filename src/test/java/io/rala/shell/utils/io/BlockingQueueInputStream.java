package io.rala.shell.utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.concurrent.BlockingQueue;

import static io.rala.shell.utils.io.Converter.convertByteArrayToCharArray;
import static io.rala.shell.utils.io.Converter.convertCharArrayToByteArray;

@SuppressWarnings("unused")
public class BlockingQueueInputStream extends InputStream {
    private final BlockingQueue<String> queue;
    private StringReader stringReader;
    private int missing;
    private boolean closeRequested = false;

    public BlockingQueueInputStream(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public int read() throws IOException {
        // this method should not be used
        int read = stringReader != null ? stringReader.read() : -1;
        if (read == -1) {
            if (readNextEntryFailed()) return -1;
            read = stringReader.read();
            missing--;
        }
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (closeRequested) return -1;
        // https://stackoverflow.com/a/36286373/2715720
        if (stringReader == null)
            if (readNextEntryFailed())
                return -1;
        char[] chars = convertByteArrayToCharArray(b);
        int read = stringReader.read(chars, off, len);
        missing -= read;
        System.arraycopy(convertCharArrayToByteArray(chars), 0, b, 0, chars.length);
        if (missing <= 0) stringReader = null;
        return read;
    }

    @Override
    public void close() {
        closeRequested = true;
    }

    @Override
    public String toString() {
        return String.join(", ", queue);
    }

    private boolean readNextEntryFailed() {
        try {
            String next = queue.take();
            stringReader = new StringReader(next);
            missing = next.length();
        } catch (InterruptedException e) {
            return true;
        }
        return false;
    }
}
