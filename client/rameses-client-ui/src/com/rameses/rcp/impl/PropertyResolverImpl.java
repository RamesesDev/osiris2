package com.rameses.rcp.impl;

import com.rameses.util.PropertyResolver;
import com.rameses.util.ValueUtil;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author jaycverg
 */
public class PropertyResolverImpl implements PropertyResolver  {
    
    public void setProperty(Object bean, String propertyName, Object value) {
        try {
            PropertyUtils.setNestedProperty(bean, propertyName, value);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public Class getPropertyType(Object bean, String propertyName) {
        try {
            return PropertyUtils.getPropertyType(bean, propertyName);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public Object getProperty(Object bean, String propertyName) {
        if ( ValueUtil.isEmpty(propertyName) ) return null;
        try {
            return PropertyUtils.getNestedProperty(bean, propertyName);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
}
