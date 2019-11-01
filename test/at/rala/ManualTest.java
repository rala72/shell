package at.rala;

import at.rala.shell.DefaultCommand;
import at.rala.shell.Shell;
import at.rala.shell.annotation.Command;

public class ManualTest implements Runnable {
    private final Shell shell;

    private ManualTest() {
        shell = new Shell(this);
        shell.register(DefaultCommand.HELP);
        shell.register(DefaultCommand.EXIT);
    }

    @Override
    public void run() {
        shell.run();
    }

    @Command
    public void echo(String line) {
        shell.printLine(line);
    }

    public static void main(String[] args) {
        new Thread(new ManualTest()).start();
    }
}
