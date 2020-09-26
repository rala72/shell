package io.rala.shell.annotation;

import io.rala.shell.Context;
import io.rala.shell.Input;

import java.lang.reflect.Parameter;
import java.util.List;

/**
 * decorator class for {@link Parameter} for {@link Command}s
 *
 * @since 1.0.0
 */
public class CommandParameter {
    private final Parameter parameter;

    CommandParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    /**
     * @return parameter which gets decorated
     * @since 1.0.0
     */
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * @return name of parameter
     * @see Parameter#getName()
     * @since 1.0.0
     */
    public String getName() {
        return getParameter().getName();
    }

    /**
     * @return {@link Optional} annotation or {@code null}
     * @see #isOptional()
     * @see Parameter#getAnnotation(Class)
     * @since 1.0.0
     */
    public Optional getOptionalAnnotation() {
        return getParameter().getAnnotation(Optional.class);
    }

    /**
     * @return parameter type
     * @see Parameter#getType()
     * @since 1.0.0
     */
    public Class<?> getType() {
        return getParameter().getType();
    }

    /**
     * @return {@code true} if {@link Optional} is present
     * @see Parameter#isAnnotationPresent(Class)
     * @since 1.0.0
     */
    public boolean isOptional() {
        return getParameter().isAnnotationPresent(Optional.class);
    }

    /**
     * @return {@code true} if {@link #isInput()},
     * {@link #isArray()} or {@link #isList()} holds
     * @since 1.0.0
     */
    public boolean isDynamic() {
        return isInput() || isArray() || isList();
    }

    /**
     * @return {@code true} if {@link Input} is assignable
     * @since 1.0.0
     */
    public boolean isInput() {
        return getType().isAssignableFrom(Input.class);
    }

    /**
     * @return {@code true} if {@link Context} is assignable
     * @since 1.0.0
     */
    public boolean isContext() {
        return getType().isAssignableFrom(Context.class);
    }

    /**
     * @return {@code true} if parameter is array or varargs
     * @since 1.0.0
     */
    public boolean isArray() {
        return getParameter().isVarArgs() || getType().isArray();
    }

    /**
     * @return {@code true} if {@link List} is assignable
     * @since 1.0.0
     */
    public boolean isList() {
        return getType().isAssignableFrom(List.class);
    }

    @Override
    public String toString() {
        return getParameter().toString();
    }
}
