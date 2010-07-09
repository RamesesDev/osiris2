/*
 * VariableResolver.java
 *
 * Created on April 28, 2010, 5:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

public interface ValueResolver {
    
    Class getType(Object bean, String name);
    Object getValue(Object bean, String name);
    void setValue(Object bean, String name, Object o);
    
}
