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
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

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
        assertThat(thread.isAlive()).isTrue();

        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            queue.put("entry\n");
            String take = readerQueue.take();
            assertThat(take).isEqualTo("entry");
        });
        thread.interrupt();
    }

    @Test
    void takeNullOnInterrupt() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertThat(thread.isAlive()).isTrue();

        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            Thread takeThread = new Thread(() -> {
                String take = readerQueue.take();
                assertThat(take).isNull();
            });
            takeThread.start();
            assertThat(takeThread.isAlive()).isTrue();
            takeThread.interrupt();
        });
        thread.interrupt();
    }

    @Test
    void peek() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertThat(thread.isAlive()).isTrue();

        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            queue.put("entry");
            String peek = readerQueue.peek();
            assertThat(peek).isNull();
            queue.put("\n");
            String take = readerQueue.take();
            assertThat(take).isEqualTo("entry");
        });
        thread.interrupt();
    }

    @Test
    void closing() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertThat(thread.isAlive()).isTrue();

        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            thread.interrupt();
            waitUntilNot(thread::isAlive);
        });

        assertThat(thread.isAlive()).isFalse();
        assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED);
    }

    @Test
    void closingOnIOException() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertThat(thread.isAlive()).isTrue();

        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            inputStream.requestIoException();
            waitUntil(readerQueue::hasException);
            waitUntilNot(thread::isAlive);
            assertThat(thread.isAlive()).isFalse();
            assertThat(readerQueue.getIOException()).isNotNull();
            assertThat(readerQueue.getInterruptedException()).isNull();
        });

        assertThat(thread.isAlive()).isFalse();
        assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED);
    }

    @Test
    void closingOnInterrupt() {
        ReaderQueue readerQueue = new ReaderQueue(bufferedReader, 1);
        Thread thread = new Thread(readerQueue);
        thread.start();
        assertThat(thread.isAlive()).isTrue();

        await().atMost(Duration.ofSeconds(TIMEOUT)).untilAsserted(() -> {
            queue.put("\n");
            waitUntil(() -> queue.isEmpty());
            queue.put("\n");
            waitUntil(() -> queue.isEmpty());
            thread.interrupt();
            waitUntil(readerQueue::hasException);
            waitUntilNot(thread::isAlive);
            assertThat(thread.isAlive()).isFalse();
            assertThat(readerQueue.getIOException()).isNull();
            assertThat(readerQueue.getInterruptedException()).isNotNull();
        });

        assertThat(thread.isAlive()).isFalse();
        assertThat(thread.getState()).isEqualTo(Thread.State.TERMINATED);
    }
}
