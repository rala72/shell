package io.rala.shell.testUtils.object;

import io.rala.shell.annotation.Command;

import java.util.List;

@SuppressWarnings("unused")
public class TestObjectWithTwoLists {
    @Command
    public void commandWithTwoLists(List<String> list1, List<String> list2) {
    }
}
