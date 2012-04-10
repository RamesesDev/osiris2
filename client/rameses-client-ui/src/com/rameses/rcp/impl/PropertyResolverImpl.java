package com.rameses.rcp.impl;

import com.rameses.common.PropertyResolver;
import com.rameses.rcp.framework.ClientContext;
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
            if(ClientContext.getCurrentContext().isDebugMode()) {
                ex.printStackTrace();
            }
        }
    }
    
    public Class getPropertyType(Object bean, String propertyName) {
        try {
            return PropertyUtils.getPropertyType(bean, propertyName);
        } catch (Exception ex) {
            if(ClientContext.getCurrentContext().isDebugMode()) {
                ex.printStackTrace();
            }
            return null;
        }
    }
    
    public Object getProperty(Object bean, String propertyName) {
        if ( ValueUtil.isEmpty(propertyName) ) return null;
        try {
            return PropertyUtils.getNestedProperty(bean, propertyName);
        } catch (Exception ex) {
            if(ClientContext.getCurrentContext().isDebugMode()) {
                ex.printStackTrace();
            }
            return null;
        }
    }
    
}
