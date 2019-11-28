package io.rala.shell.testUtils.object;

import io.rala.shell.Input;
import io.rala.shell.annotation.Command;
import io.rala.shell.annotation.Optional;
import io.rala.shell.testUtils.TestContext;

import java.util.List;

@SuppressWarnings("unused")
public class TestObject {
    private TestContext context;

    public void setContext(TestContext context) {
        this.context = context;
    }

    @Command
    public void methodWithOneInputParameter(Input input) {
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
    public void methodWithOneStringAndStringVarargsParameter(String s, String... v) {
    }

    @Command
    public void methodWithOneStringArrayParameter(String[] s) {
    }

    @Command
    public void methodWithOneStringListParameter(List<String> s) {
    }

    // endregion

    // region string optional

    @Command
    public void methodWithOneOptionalStringParameter(@Optional String o) {
    }

    @Command
    public void methodWithOneStringAndOptionalStringParameter(String s, @Optional String o) {
    }

    @Command
    public void methodWithOneOptionalStringParameterWithDefaultValue(@Optional("null") String o) {
    }

    // endregion

    // region string return

    @Command
    public String methodWithOneStringParameterWhichReturns(String s) {
        return s;
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
    public void methodWithOneCharacterPrimitiveParameter(char c) {
        printLine(String.valueOf(c));
    }

    @Command
    public void methodWithOneCharacterObjectParameter(Character c) {
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
    public void methodWithOneIntegerPrimitiveParameter(int i) {
        printLine(String.valueOf(i));
    }

    @Command
    public void methodWithOneIntegerObjectParameter(Integer i) {
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

    // region primitive optional

    @Command
    public void methodWithOneOptionalIntegerParameter(@Optional int i) {
        context.printLine(String.valueOf(i));
    }

    @Command
    public void methodWithOneOptionalIntegerParameterWithDefaultValue(@Optional("1") int i) {
        context.printLine(String.valueOf(i));
    }

    // endregion

    // region exception

    @Command
    public void exceptionCommandWithoutMessage() {
        throw new RuntimeException();
    }

    @Command
    public void exceptionCommandWithMessage() {
        throw new RuntimeException("message");
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
