package io.rala.shell.testUtils.object;

import io.rala.shell.annotation.Command;
import io.rala.shell.annotation.Optional;

@SuppressWarnings("unused")
public class TestObjectWithOptional {
    @Command
    public void commandWithOptional(String s, @Optional String optional) {
    }
}
