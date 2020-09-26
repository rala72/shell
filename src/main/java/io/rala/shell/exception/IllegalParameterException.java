package io.rala.shell.exception;

import io.rala.shell.Input;
import io.rala.shell.annotation.Optional;

/**
 * indicates that a parameter is invalid
 * <p>
 * rules are
 * <ul>
 *     <li>only one dynamic parameter (array, varargs, list)</li>
 *     <li>only last parameter may be dynamic</li>
 *     <li>if {@link Input} is present it is the only parameter</li>
 *     <li>only last parameters may be {@link Optional}</li>
 *     <li>default value of {@link Optional} has to be valid parsable</li>
 * </ul>
 *
 * @since 1.0.0
 */
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

    /**
     * @param message message of exception
     * @since 1.0.0
     */
    protected IllegalParameterException(String message) {
        super(message);
    }

    /**
     * @param method method name of command
     * @return new {@link IllegalParameterException}
     * @see IllegalParameterException
     * @since 1.0.0
     */
    public static IllegalParameterException createNewOnlyOneDynamicInstance(String method) {
        return new IllegalParameterException(method + SUFFIX_ONLY_ONE_DYNAMIC);
    }

    /**
     * @param method method name of command
     * @return new {@link IllegalParameterException}
     * @see IllegalParameterException
     * @since 1.0.0
     */
    public static IllegalParameterException createNewOnlyLastDynamicInstance(String method) {
        return new IllegalParameterException(method + SUFFIX_ONLY_LAST_DYNAMIC);
    }

    /**
     * @param method method name of command
     * @return new {@link IllegalParameterException}
     * @see IllegalParameterException
     * @since 1.0.0
     */
    public static IllegalParameterException createNewIfInputNoOther(String method) {
        return new IllegalParameterException(method + SUFFIX_IF_INPUT_NO_OTHER);
    }

    /**
     * @param method method name of command
     * @return new {@link IllegalParameterException}
     * @see IllegalParameterException
     * @since 1.0.0
     */
    public static IllegalParameterException createNewOnlyLastParametersCanBeAbsent(String method) {
        return new IllegalParameterException(method + SUFFIX_ONLY_LAST_PARAMETERS_CAN_BE_ABSENT);
    }

    /**
     * @param param param name of method
     * @return new {@link IllegalParameterException}
     * @see IllegalParameterException
     * @since 1.0.0
     */
    public static IllegalParameterException createNewOptionalDefaultValueIsInvalid(String param) {
        return new IllegalParameterException(param + SUFFIX_DEFAULT_VALUE_INVALID);
    }
}
