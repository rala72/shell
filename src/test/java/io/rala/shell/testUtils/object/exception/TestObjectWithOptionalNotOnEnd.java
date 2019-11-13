package io.rala.shell.testUtils.object.exception;

import io.rala.shell.annotation.Command;
import io.rala.shell.annotation.Optional;

@SuppressWarnings("unused")
public class TestObjectWithOptionalNotOnEnd {
    @Command
    public void commandWithOptionalNotOnEnd(@Optional String optional, String s) {
    }
}
