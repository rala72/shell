package io.rala.shell;

import io.rala.shell.testUtils.io.BlockingQueueInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static io.rala.shell.testUtils.WaitUtils.waitUntil;
import static io.rala.shell.testUtils.WaitUtils.waitUntilNot;
import static org.junit.jupiter.api.Assertions.*;

class ReaderQueueTest {
    private static final int TIMEOUT = 1;
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
        assertTrue(thread.isAlive());

        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            queue.put("entry\n");
            String take = readerQueue.take();
            assertEquals("entry", take);
        });
        thread.interrupt();
    }

    @Test
    void takeNullOnInterrupt() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertTrue(thread.isAlive());

        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            Thread takeThread = new Thread(() -> {
                String take = readerQueue.take();
                assertNull(take);
            });
            takeThread.start();
            assertTrue(takeThread.isAlive());
            takeThread.interrupt();
        });
        thread.interrupt();
    }

    @Test
    void peek() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertTrue(thread.isAlive());

        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            queue.put("entry");
            String peek = readerQueue.peek();
            assertNull(peek);
            queue.put("\n");
            String take = readerQueue.take();
            assertEquals("entry", take);
        });
        thread.interrupt();
    }

    @Test
    void closing() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertTrue(thread.isAlive());

        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            thread.interrupt();
            waitUntilNot(thread::isAlive);
        });

        assertFalse(thread.isAlive());
        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void closingOnIOException() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertTrue(thread.isAlive());

        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            inputStream.requestIoException();
            waitUntil(readerQueue::hasException);
            waitUntilNot(thread::isAlive);
            assertFalse(thread.isAlive());
            assertNotNull(readerQueue.getIOException());
            assertNull(readerQueue.getInterruptedException());
        });

        assertFalse(thread.isAlive());
        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }

    @Test
    void closingOnInterrupt() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader, 1);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertTrue(thread.isAlive());

        assertTimeoutPreemptively(Duration.ofSeconds(TIMEOUT), () -> {
            queue.put("\n");
            waitUntil(() -> queue.isEmpty());
            queue.put("\n");
            waitUntil(() -> queue.isEmpty());
            thread.interrupt();
            waitUntil(readerQueue::hasException);
            waitUntilNot(thread::isAlive);
            assertFalse(thread.isAlive());
            assertNull(readerQueue.getIOException());
            assertNotNull(readerQueue.getInterruptedException());
        });

        assertFalse(thread.isAlive());
        assertEquals(thread.getState(), Thread.State.TERMINATED);
    }
}
