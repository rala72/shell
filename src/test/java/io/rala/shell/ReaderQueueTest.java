package io.rala.shell;

import io.rala.shell.utils.io.BlockingQueueInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static io.rala.shell.utils.WaitUtils.waitUntil;

class ReaderQueueTest {
    private static final int TIMEOUT = 5;
    private BlockingQueue<String> queue;
    private BlockingQueueInputStream inputStream;
    private BufferedReader bufferedReader;

    @BeforeEach
    void setUp() {
        queue = new LinkedBlockingQueue<>();
        inputStream = new BlockingQueueInputStream(queue);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Test
    void take() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        Assertions.assertTrue(thread.isAlive());

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            queue.put("entry\n");
            String take = readerQueue.take();
            Assertions.assertEquals("entry", take);
        });
        thread.interrupt();
    }

    @Test
    void takeNullOnInterrupt() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        Assertions.assertTrue(thread.isAlive());

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            Thread takeThread = new Thread(() -> {
                String take = readerQueue.take();
                Assertions.assertNull(take);
            });
            takeThread.start();
            Assertions.assertTrue(takeThread.isAlive());
            takeThread.interrupt();
        });
        thread.interrupt();
    }

    @Test
    void peek() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        Assertions.assertTrue(thread.isAlive());

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            queue.put("entry");
            queue.put("\n");
            String peek = readerQueue.peek();
            Assertions.assertNull(peek);
            String take = readerQueue.take();
            Assertions.assertEquals("entry", take);
        });
        thread.interrupt();
    }

    @Test
    void closing() throws InterruptedException {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        Assertions.assertTrue(thread.isAlive());

        thread.interrupt();

        Thread.sleep(100);
        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void closingOnIOException() throws InterruptedException {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        Assertions.assertTrue(thread.isAlive());

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            inputStream.requestIoException();
            waitUntil(readerQueue::hasException);
            Thread.sleep(100);
            Assertions.assertFalse(thread.isAlive());
            Assertions.assertNotNull(readerQueue.getIOException());
            Assertions.assertNull(readerQueue.getInterruptedException());
        });

        Thread.sleep(100);
        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void closingOnInterrupt() throws InterruptedException {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader, 1);
        Thread thread = new Thread(readerQueue);
        thread.start();
        Assertions.assertTrue(thread.isAlive());

        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            queue.put("\n");
            waitUntil(() -> queue.isEmpty());
            queue.put("\n");
            waitUntil(() -> queue.isEmpty());
            thread.interrupt();
            waitUntil(readerQueue::hasException);
            Thread.sleep(100);
            Assertions.assertFalse(thread.isAlive());
            Assertions.assertNull(readerQueue.getIOException());
            Assertions.assertNotNull(readerQueue.getInterruptedException());
        });

        Thread.sleep(100);
        Assertions.assertFalse(thread.isAlive());
        Assertions.assertEquals(thread.getState(), Thread.State.TERMINATED);
    }
}
