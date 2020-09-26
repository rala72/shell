package io.rala.shell.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * optional optional annotation to indicate this parameter can be omitted
 *
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Optional {
    /**
     * optional default value<br>
     * default value is {@code null}
     *
     * @return default value
     * @since 1.0.0
     */
    String value() default "";
}
