package io.rala.shell.testUtils.object;

import io.rala.shell.annotation.Command;
import io.rala.shell.annotation.Optional;

@SuppressWarnings("unused")
public class TestObjectWithOptionalDefaultValue {
    @Command
    public void commandWithOptional(String s, @Optional("null") String optional) {
    }
}
