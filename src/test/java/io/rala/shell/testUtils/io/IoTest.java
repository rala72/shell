package io.rala.shell.testUtils.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

class IoTest {
    @Test
    void newLines() throws InterruptedException {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        HistoryOutputStream historyOutputStream = new HistoryOutputStream();
        transferInputToOutput(queue, historyOutputStream);

        BlockingQueue<String> commands = new LinkedBlockingQueue<>();
        commands.add("test1\n");
        commands.add("test2\n");
        commands.add("test3\n");
        queue.addAll(commands);

        Queue<String> history = saveOutputHistory(historyOutputStream, commands);
        assertQueuesAreEqual(filterLineBreaks(commands), history);
    }

    @Test
    void joiningWithNewLineOnLastElement() throws InterruptedException {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        HistoryOutputStream historyOutputStream = new HistoryOutputStream();
        transferInputToOutput(queue, historyOutputStream);

        BlockingQueue<String> commands = new LinkedBlockingQueue<>();
        commands.add("test1");
        commands.add("test2");
        commands.add("test3\n");
        queue.addAll(commands);

        Queue<String> history = saveOutputHistory(historyOutputStream, commands);
        assertQueuesAreJoinedEqual(filterLineBreaks(commands), history);
    }

    @Test
    void joiningWithExtraNewLine() throws InterruptedException {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        HistoryOutputStream historyOutputStream = new HistoryOutputStream();
        transferInputToOutput(queue, historyOutputStream);

        BlockingQueue<String> commands = new LinkedBlockingQueue<>();
        commands.add("test1");
        commands.add("test2");
        commands.add("test3");
        commands.add("\n");
        queue.addAll(commands);

        Queue<String> history = saveOutputHistory(historyOutputStream, commands);
        assertQueuesAreJoinedEqual(filterLineBreaks(commands), history);
    }

    // region queues handling

    private static void transferInputToOutput(
        BlockingQueue<String> input, HistoryOutputStream output
    ) {
        BlockingQueueInputStream blockingQueueInputStream = new BlockingQueueInputStream(input);
        new Thread(() -> {
            try {
                blockingQueueInputStream.transferTo(output);
            } catch (IOException ignored) {
                blockingQueueInputStream.close();
            }
        }).start();
    }

    private static Queue<String> saveOutputHistory(
        HistoryOutputStream historyOutputStream, Queue<String> input
    ) throws InterruptedException {
        BlockingQueue<String> history = new LinkedBlockingQueue<>();
        for (int i = 0; i < countPhrase(input, "\n"); i++)
            history.add(historyOutputStream.getHistory().take());
        return history;
    }

    private static BlockingQueue<String> filterLineBreaks(Queue<String> queue) {
        return queue.parallelStream()
            .map(s -> s.replace("\n", ""))
            .collect(Collectors.toCollection(LinkedBlockingQueue::new));
    }

    // endregion

    // region countPhrase

    @SuppressWarnings("SameParameterValue")
    private static int countPhrase(Collection<String> strings, String phrase) {
        return countPhrase(String.join("", strings), phrase);
    }

    private static int countPhrase(String string, String phrase) {
        return string.length() - string.replaceAll(phrase, "").length();
    }

    // endregion

    // region assert queues

    private static void assertQueuesAreEqual(Queue<String> queue1, Queue<String> queue2) {
        Assertions.assertEquals(String.join("", queue1), String.join("", queue2));
    }

    private static void assertQueuesAreJoinedEqual(Queue<String> queue1, Queue<String> queue2) {
        Assertions.assertEquals(
            String.join("", queue1),
            String.join("", queue2)
        );
    }

    // endregion
}
