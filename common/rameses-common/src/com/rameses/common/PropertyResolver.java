/*
 * PropertyResolver.java
 *
 * Created on June 7, 2010, 8:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.common;

/**
 *
 * @author elmo
 */
public interface PropertyResolver {
    
    void setProperty(Object bean, String propertyName, Object value );
    Class getPropertyType(Object bean, String propertyName );
    Object getProperty( Object bean, String propertyName );
}
