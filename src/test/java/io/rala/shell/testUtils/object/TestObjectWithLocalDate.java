package io.rala.shell.testUtils.object;

import io.rala.shell.Context;
import io.rala.shell.annotation.Command;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class TestObjectWithLocalDate {
    @Command
    public void commandWithLocalDate(LocalDate localDate, Context context) {
        context.printLine(localDate.toString());
    }
}
