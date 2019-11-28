package io.rala.shell.annotation;

import io.rala.shell.Context;
import io.rala.shell.Input;

import java.lang.reflect.Parameter;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CommandParameter {
    private final Parameter parameter;

    CommandParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getName() {
        return getParameter().getName();
    }

    public Optional getOptionalAnnotation() {
        return getParameter().getAnnotation(Optional.class);
    }

    public Class<?> getType() {
        return getParameter().getType();
    }

    public boolean isOptional() {
        return getParameter().isAnnotationPresent(Optional.class);
    }

    public boolean isDynamic() {
        return isInput() || isArray() || isList();
    }

    public boolean isInput() {
        return getParameter().getType().isAssignableFrom(Input.class);
    }

    public boolean isContext() {
        return getParameter().getType().isAssignableFrom(Context.class);
    }

    public boolean isArray() {
        return getParameter().isVarArgs() || getParameter().getType().isArray();
    }

    public boolean isList() {
        return getParameter().getType().isAssignableFrom(List.class);
    }

    @Override
    public String toString() {
        return getParameter().toString();
    }
}
