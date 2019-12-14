package io.rala.shell.exception;

public class IllegalParameterException extends RuntimeException {
    private static final String SUFFIX_ONLY_ONE_DYNAMIC =
        ": may only have one dynamic parameter";
    private static final String SUFFIX_ONLY_LAST_DYNAMIC =
        ": only last parameter may be dynamic";
    private static final String SUFFIX_IF_INPUT_NO_OTHER =
        ": if input present, no other parameter allowed";
    private static final String SUFFIX_ONLY_LAST_PARAMETERS_CAN_BE_ABSENT =
        ": only last parameters can be absent";
    private static final String SUFFIX_DEFAULT_VALUE_INVALID =
        ": default value is invalid";

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

    public static IllegalParameterException createNewOnlyLastParametersCanBeAbsent(String method) {
        return new IllegalParameterException(method + SUFFIX_ONLY_LAST_PARAMETERS_CAN_BE_ABSENT);
    }

    public static IllegalParameterException createNewOptionalDefaultValueIsInvalid(String param) {
        return new IllegalParameterException(param + SUFFIX_DEFAULT_VALUE_INVALID);
    }
}
