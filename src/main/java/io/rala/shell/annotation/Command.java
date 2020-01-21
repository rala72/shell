package io.rala.shell.annotation;

import io.rala.shell.Shell;
import io.rala.shell.command.HelpCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * command annotation to indicate that the method
 * should be registered in {@link Shell}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    /**
     * optional command name<br>
     * default value is method name<br>
     * name must be unique
     *
     * @return command name of method
     */
    String value() default "";

    /**
     * optional documentation
     * which is used for example in {@link HelpCommand}
     *
     * @return documentation of method
     */
    String documentation() default "";

    /**
     * optional usage
     * which is used for example in {@link HelpCommand}
     *
     * @return usage of method
     */
    String usage() default "";
}
