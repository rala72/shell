package at.rala;

import at.rala.shell.DefaultCommand;
import at.rala.shell.Shell;

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

    public static void main(String[] args) {
        new Thread(new ManualTest()).start();
    }
}
