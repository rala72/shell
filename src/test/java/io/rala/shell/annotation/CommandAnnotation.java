package io.rala.shell.annotation;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class CommandAnnotation implements Command {
    private final String value;
    private final String documentation;
    private final String usage;

    public CommandAnnotation() {
        this(null);
    }

    public CommandAnnotation(String value) {
        this(value, null);
    }

    public CommandAnnotation(String value, String documentation) {
        this(value, documentation, null);
    }

    public CommandAnnotation(String value, String documentation, String usage) {
        this.value = value;
        this.documentation = documentation;
        this.usage = usage;
    }

    @Override
    public String value() {
        return value != null ? value : "";
    }

    @Override
    public String documentation() {
        return documentation != null ? documentation : "";
    }

    @Override
    public String usage() {
        return usage != null ? usage : "";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Annotation.class;
    }
}
