package com.peterphi.std.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface IndexServerProperty {
    /**
     * A property Name
     *
     * @return
     */
    public String name();

    /**
     * The property key to look up
     *
     * @return
     */
    public String propertyKey();
}
