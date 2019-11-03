package at.rala.shell.utils;

import at.rala.shell.annotation.Command;

@SuppressWarnings("unused")
public class TestObject {
    private TestContext context;

    public void setContext(TestContext context) {
        this.context = context;
    }

    // region string

    @Command
    public void methodWithoutParameter() {
    }

    @Command
    public void methodWithOneStringParameter(String s) {
    }

    @Command
    public void methodWithTwoStringParameter(String s1, String s2) {
    }

    @Command
    public void methodWithOneStringVarargsParameter(String... s) {
    }

    @Command
    public void methodWithOneStringArrayParameter(String[] s) {
    }

    // endregion

    // region primitive

    @Command
    public void methodWithOneBooleanPrimitiveParameter(boolean b) {
        printLine(String.valueOf(b));
    }

    @Command
    public void methodWithOneBooleanObjectParameter(Boolean b) {
        printLine(String.valueOf(b));
    }

    @Command
    public void methodWithOneBytePrimitiveParameter(byte b) {
        printLine(String.valueOf(b));
    }

    @Command
    public void methodWithOneByteObjectParameter(Byte b) {
        printLine(String.valueOf(b));
    }

    @Command
    public void methodWithOneCharPrimitiveParameter(char c) {
        printLine(String.valueOf(c));
    }

    @Command
    public void methodWithOneCharObjectParameter(Character c) {
        printLine(String.valueOf(c));
    }

    @Command
    public void methodWithOneShortPrimitiveParameter(short s) {
        printLine(String.valueOf(s));
    }

    @Command
    public void methodWithOneShortObjectParameter(Short s) {
        printLine(String.valueOf(s));
    }

    @Command
    public void methodWithOneIntPrimitiveParameter(int i) {
        printLine(String.valueOf(i));
    }

    @Command
    public void methodWithOneIntObjectParameter(Integer i) {
        printLine(String.valueOf(i));
    }

    @Command
    public void methodWithOneLongPrimitiveParameter(long l) {
        printLine(String.valueOf(l));
    }

    @Command
    public void methodWithOneLongObjectParameter(Long l) {
        printLine(String.valueOf(l));
    }

    @Command
    public void methodWithOneFloatPrimitiveParameter(float f) {
        printLine(String.valueOf(f));
    }

    @Command
    public void methodWithOneFloatObjectParameter(Float f) {
        printLine(String.valueOf(f));
    }

    @Command
    public void methodWithOneDoublePrimitiveParameter(double d) {
        printLine(String.valueOf(d));
    }

    @Command
    public void methodWithOneDoubleObjectParameter(Double d) {
        printLine(String.valueOf(d));
    }

    // endregion

    // region exception

    @Command
    public void exceptionCommand() {
        throw new RuntimeException();
    }

    @Command
    private void illegalAccessCommand() {
    }

    // endregion

    @Command(value = "value", documentation = "documentation", usage = "usage")
    public void commandWithAttributes() {
    }

    @Override
    public String toString() {
        return "TestObject";
    }

    private void printLine(String s) {
        if (context == null) return;
        context.printLine(s);
    }
}
