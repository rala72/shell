package io.rala;

import io.rala.shell.DefaultCommand;
import io.rala.shell.Shell;
import io.rala.shell.annotation.Command;
import io.rala.shell.annotation.Optional;

@SuppressWarnings("unused")
public class ManualTest implements Runnable {
    private final Shell shell;

    private ManualTest() {
        shell = new Shell();
        shell.register(this);
        shell.register(DefaultCommand.values());
    }

    @Override
    public void run() {
        shell.run();
    }

    @Command(documentation = "prints arguments written afterwards", usage = "echo [text [text...]]")
    public void echo(String s1, String s2, @Optional String s3) {
        shell.printLine(String.join(" ", s1, s2, s3 == null ? "" : s3));
    }

    public static void main(String[] args) {
        new Thread(new ManualTest()).start();
    }
}
