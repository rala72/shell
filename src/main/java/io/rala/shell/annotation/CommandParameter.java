package io.rala.shell.annotation;

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

    public Class<?> getType() {
        return getParameter().getType();
    }

    public boolean isInput() {
        return getParameter().getType().isAssignableFrom(Input.class);
    }

    public boolean isDynamic() {
        return isInput() || isArray() || isList();
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
