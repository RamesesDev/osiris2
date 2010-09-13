/*
 * DefaultPropertyResolver.java
 *
 * Created on June 7, 2010, 8:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.custom;


import com.rameses.common.PropertyResolver;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author elmo
 */
public class DefaultPropertyResolver implements PropertyResolver  {
    
    /** Creates a new instance of DefaultPropertyResolver */
    public DefaultPropertyResolver() {
    }
    
    public void setProperty(Object bean, String propertyName, Object value) {
        try {
            PropertyUtils.setNestedProperty(bean,propertyName,value);
        } catch(Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    
    public Class getPropertyType(Object bean, String propertyName) {
        try {
            return PropertyUtils.getPropertyType( bean, propertyName);
        } catch(Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    
    public Object getProperty(Object bean, String propertyName) {
        try{
            return PropertyUtils.getNestedProperty(bean, propertyName);
        } catch(Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    
}
