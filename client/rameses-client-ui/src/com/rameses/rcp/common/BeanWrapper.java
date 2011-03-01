/*
 * BeanWrapper.java
 *
 * Created on February 18, 2011, 1:59 PM
 * @author jaycverg
 */

package com.rameses.rcp.common;

import com.rameses.common.PropertyResolver;
import com.rameses.rcp.framework.ClientContext;
import java.util.HashMap;
import java.util.Map;


public class BeanWrapper extends HashMap {
    
    private Object bean;
    
    public BeanWrapper(Object bean, Map extended) {
        this.bean = bean;
        if( extended != null ) super.putAll(extended);
    }
    
    public Object put(Object key, Object value) {
        if( super.containsKey(key) )
            return super.put(key, value);
        
        try {
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            res.setProperty(bean, key.toString(), value);
        } catch(Exception e) {;}
                
        return value;
    }
    
    public Object get(Object key) {
        Object value = super.get(key);
        if( value != null ) return value;
        
        try {
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            return res.getProperty(bean, key.toString());
        } catch(Exception e) {;}
        
        
        return null;
    }
    
}
