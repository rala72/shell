package at.rala.shell.annotation;

@SuppressWarnings({"unused", "WeakerAccess"})
class TestObject {
    @Command
    public void simpleCommandWithoutParameter() {
    }

    @Command
    public void simpleCommandWithOneParameter(String s) {
    }

    @Command
    public void simpleCommandWithTwoParameter(String s1, String s2) {
    }

    @Command
    public void simpleCommandWithVarargsParameter(String... s) {
    }

    @Command
    public void simpleCommandWithArrayParameter(String[] s) {
    }
}
