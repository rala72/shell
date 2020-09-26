package io.rala.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * {@link BlockingQueue} for {@link BufferedReader}
 *
 * @since 1.0.0
 */
public class ReaderQueue implements Runnable {
    // TO DO: remove RQ to fix shell not stopping on request
    private final BlockingQueue<String> queue;
    private final BufferedReader bufferedReader;
    private IOException ioException = null;
    private InterruptedException interruptedException = null;

    /**
     * new reader queue with {@link Integer#MAX_VALUE} capacity
     *
     * @param bufferedReader buffered reader to queue
     * @see #ReaderQueue(BufferedReader, int)
     * @since 1.0.0
     */
    protected ReaderQueue(BufferedReader bufferedReader) {
        this(bufferedReader, Integer.MAX_VALUE);
    }

    /**
     * @param bufferedReader buffered reader to queue
     * @param capacity       capacity of {@link BlockingQueue}
     * @since 1.0.0
     */
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

    /**
     * @return {@link BlockingQueue#take()} or
     * {@code null} if {@link InterruptedException}
     * @since 1.0.0
     */
    public final String take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    /**
     * @return {@link BlockingQueue#peek()}
     * @since 1.0.0
     */
    public final String peek() {
        return queue.peek();
    }

    /**
     * @return {@code true} if {@link #getIOException()} or
     * {@link #getInterruptedException()} is not {@code null}
     * @since 1.0.0
     */
    protected boolean hasException() {
        return getIOException() != null || getInterruptedException() != null;
    }

    /**
     * @return caught exception from {@link #run()}
     * @since 1.0.0
     */
    protected IOException getIOException() {
        return ioException;
    }

    /**
     * @return caught exception from {@link #run()}
     * @since 1.0.0
     */
    protected InterruptedException getInterruptedException() {
        return interruptedException;
    }
}
