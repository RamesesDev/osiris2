package com.rameses.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface ChangeLog {
    
    String[] prefix() default {"."};
    
}
