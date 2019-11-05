package io.rala.shell.exception;

@SuppressWarnings({"unused", "WeakerAccess"})
public class IllegalParameterException extends RuntimeException {
    private static final String SUFFIX_ONLY_ONE_DYNAMIC =
        ": may only have one dynamic parameter";
    private static final String SUFFIX_ONLY_LAST_DYNAMIC =
        ": only last parameter may be dynamic";
    private static final String SUFFIX_IF_INPUT_NO_OTHER =
        ": if input present, no other parameter allowed";

    protected IllegalParameterException(String message) {
        super(message);
    }

    public static IllegalParameterException createNewOnlyOneDynamicInstance(String method) {
        return new IllegalParameterException(method + SUFFIX_ONLY_ONE_DYNAMIC);
    }

    public static IllegalParameterException createNewOnlyLastDynamicInstance(String method) {
        return new IllegalParameterException(method + SUFFIX_ONLY_LAST_DYNAMIC);
    }

    public static IllegalParameterException createNewIfInputNoOther(String method) {
        return new IllegalParameterException(method + SUFFIX_IF_INPUT_NO_OTHER);
    }
}
