package io.rala.shell.utils.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@SuppressWarnings("SameParameterValue")
class IoTest {
    @Test
    void testNewLines() throws InterruptedException {
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
        commands.add("test1\n");
        commands.add("test2\n");
        commands.add("test3\n");
        queue.addAll(commands);

        BlockingQueue<String> history = new LinkedBlockingQueue<>();
        BlockingQueue<String> filterLineBreaks = filterLineBreaks(commands);
        for (int i = 0; i < countPhrase(commands, "\n"); i++)
            history.add(historyOutputStream.getHistory().take());
        blockingQueueInputStream.close();
        assertQueuesAreEqual(filterLineBreaks, history);
    }

    @Test
    void testJoiningWithNewLineOnLastElement() throws InterruptedException {
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
        commands.add("test1");
        commands.add("test2");
        commands.add("test3\n");
        queue.addAll(commands);

        BlockingQueue<String> history = new LinkedBlockingQueue<>();
        BlockingQueue<String> filterLineBreaks = filterLineBreaks(commands);
        for (int i = 0; i < countPhrase(commands, "\n"); i++)
            history.add(historyOutputStream.getHistory().take());
        blockingQueueInputStream.close();
        Assertions.assertEquals(
            String.join("", filterLineBreaks),
            String.join("", history)
        );
    }

    @Test
    void testJoiningWithExtraNewLine() throws InterruptedException {
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
        commands.add("test1");
        commands.add("test2");
        commands.add("test3");
        commands.add("\n");
        queue.addAll(commands);

        BlockingQueue<String> history = new LinkedBlockingQueue<>();
        BlockingQueue<String> filterLineBreaks = filterLineBreaks(commands);
        for (int i = 0; i < countPhrase(commands, "\n"); i++)
            history.add(historyOutputStream.getHistory().take());
        blockingQueueInputStream.close();
        Assertions.assertEquals(
            String.join("", filterLineBreaks),
            String.join("", history)
        );
    }

    private static int countPhrase(Collection<String> strings, String phrase) {
        return countPhrase(String.join("", strings), phrase);
    }

    private static int countPhrase(String string, String phrase) {
        return string.length() - string.replaceAll(phrase, "").length();
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
