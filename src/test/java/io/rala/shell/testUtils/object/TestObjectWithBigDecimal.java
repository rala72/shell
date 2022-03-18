package io.rala.shell.testUtils.object;

import io.rala.shell.Context;
import io.rala.shell.annotation.Command;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class TestObjectWithBigDecimal {
    @Command
    public void commandWithBigDecimal(BigDecimal bigDecimal, Context context) {
        context.printLine(bigDecimal.toString());
    }
}
