package com.rameses.rcp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface Async {
    
    String host() default "";
    boolean loop() default false;
    String responseHandler() default "";
    String connection() default "";
    
}
