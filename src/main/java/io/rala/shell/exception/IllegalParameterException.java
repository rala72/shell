package io.rala.shell.exception;

@SuppressWarnings({"unused", "WeakerAccess", "FieldCanBeLocal"})
public class IllegalParameterException extends RuntimeException {
    private static final String SUFFIX_ONLY_ONE_ARRAY =
        ": may only have one array parameter";
    private static final String SUFFIX_ONLY_LAST_ARRAY_OR_VARARG =
        ": only last parameter may be an array or vararg";

    protected IllegalParameterException(String message) {
        super(message);
    }

    public static IllegalParameterException createNewOnlyOneArrayInstance(String method) {
        return new IllegalParameterException(method + SUFFIX_ONLY_ONE_ARRAY);
    }

    public static IllegalParameterException createNewOnlyLastArrayOrVararg(String method) {
        return new IllegalParameterException(method + SUFFIX_ONLY_LAST_ARRAY_OR_VARARG);
    }
}
