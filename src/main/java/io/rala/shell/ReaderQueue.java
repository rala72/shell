package io.rala.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ReaderQueue implements Runnable {
    // TO DO: remove RQ to fix shell not stopping on request
    private final BlockingQueue<String> queue;
    private final BufferedReader bufferedReader;
    private IOException ioException = null;
    private InterruptedException interruptedException = null;

    protected ReaderQueue(BufferedReader bufferedReader) {
        this(bufferedReader, Integer.MAX_VALUE);
    }

    protected ReaderQueue(BufferedReader bufferedReader, int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run() {
        try {
            while (Thread.currentThread().isAlive()) {
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException();
                if (!bufferedReader.ready()) continue;
                String line = bufferedReader.readLine();
                if (line == null) break;
                queue.put(line.trim());
            }
        } catch (IOException e) {
            ioException = e;
        } catch (InterruptedException e) {
            interruptedException = e;
        }
    }

    public final String take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    public final String peek() {
        return queue.peek();
    }

    protected boolean hasException() {
        return getIOException() != null || getInterruptedException() != null;
    }

    protected IOException getIOException() {
        return ioException;
    }

    protected InterruptedException getInterruptedException() {
        return interruptedException;
    }
}
