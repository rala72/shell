package io.rala.shell.utils.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

class IoTest {
    @Test
    void test() throws InterruptedException {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        BlockingQueueInputStream blockingQueueInputStream = new BlockingQueueInputStream(queue);
        HistoryOutputStream historyOutputStream = new HistoryOutputStream();
        new Thread(() -> {
            try {
                blockingQueueInputStream.transferTo(historyOutputStream);
            } catch (IOException ignored) {
            }
        }).start();

        BlockingQueue<String> commands = new LinkedBlockingQueue<>();
        commands.add("test\n");
        commands.add("test\n");
        commands.add("test\n");
        commands.forEach(s -> {
            try {
                queue.put(s);
            } catch (InterruptedException ignored) {
            }
        });

        BlockingQueue<String> history = new LinkedBlockingQueue<>();
        for (int i = 0; i < commands.size(); i++)
            history.add(historyOutputStream.getHistory().take());
        blockingQueueInputStream.close();
        assertQueuesAreEqual(filterLineBreaks(commands), history);
    }

    private static BlockingQueue<String> filterLineBreaks(Queue<String> queue) {
        return queue.stream()
            .map(s -> s.replace("\n", ""))
            .collect(Collectors.toCollection(LinkedBlockingQueue::new));
    }

    private static void assertQueuesAreEqual(Queue<String> queue1, Queue<String> queue2) {
        Assertions.assertEquals(String.join("", queue1), String.join("", queue2));
    }
}
