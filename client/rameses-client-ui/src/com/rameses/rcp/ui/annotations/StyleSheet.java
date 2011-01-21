/*
 * StyleSheet.java
 *
 * Created on July 23, 2010, 4:09 PM
 * @author jaycverg
 */

package com.rameses.rcp.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface StyleSheet {
    String[] value() default {};
}
