/*
 * SelectItemsConverter.java
 *
 * Created on June 11, 2010, 9:54 AM
 * @author jaycverg
 */

package com.rameses.web.component.select;

import java.util.Collection;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import org.apache.commons.beanutils.BeanUtils;


public class SelectItemsConverter implements Converter {
    
    public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
        UIComponent child = null;
        if(comp.getChildren().size() > 0) {
            child = comp.getChildren().get(0);
        }
        if(child == null) {
            return null;
        }
        
        String keyAttr = (String) child.getAttributes().get("key");
        ValueBinding valueVb = child.getValueBinding("value");
        Class clazz = valueVb.getType(ctx);
        Object items = valueVb.getValue(ctx);
        
        if ( clazz.isEnum() ) {
            Enum []list = (Enum[]) clazz.getEnumConstants();
            for(Object obj: list) {
                String key = getProperty(obj, keyAttr);
                if(key.equals(value)){
                    return obj;
                }
            }
        } else if ( clazz.isArray() && items != null ) {
            for (Object obj: (Object[]) items) {
                String key = getProperty(obj, keyAttr);
                if(key.equals(value)){
                    return obj;
                }
            }
        } else if ( items instanceof Collection && items != null ) {
            for(Object obj : (Collection) items){
                String key = getProperty(obj, keyAttr);
                if(key.equals(value)){
                    return obj;
                }
            }
        }
        
        return null;
    }
    
    public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
        if(value == null) return null;
        if(value instanceof String) return value+"";
        
        UIComponent child = null;
        if(comp.getChildren().size() > 0) {
            child = comp.getChildren().get(0);
        }
        if(child == null) {
            return null;
        }
        
        String keyAttr = (String) child.getAttributes().get("key");
        return getProperty(value, keyAttr);
    }
    
    private String getProperty(Object bean, String property) {
        String value = null;
        if ( property != null && property.trim().length() > 0) {
            try {
                value = BeanUtils.getNestedProperty(bean, property);
            } catch (Exception ex) {;}
        }
        return value == null? bean.toString() : value;
    }
    
}
