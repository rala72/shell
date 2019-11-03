package io.rala;

import io.rala.shell.DefaultCommand;
import io.rala.shell.Shell;
import io.rala.shell.annotation.Command;

public class ManualTest implements Runnable {
    private final Shell shell;

    private ManualTest() {
        shell = new Shell();
        shell.register(this);
        shell.register(DefaultCommand.HELP);
        shell.register(DefaultCommand.EXIT);
    }

    @Override
    public void run() {
        shell.run();
    }

    @Command(documentation = "prints arguments written afterwards", usage = "echo [text [text...]]")
    public void echo(String... line) {
        shell.printLine(String.join(" ", line));
    }

    public static void main(String[] args) {
        new Thread(new ManualTest()).start();
    }
}
