/*
 * EnumItemsConverter.java
 *
 * Created on May 13, 2009, 12:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.component.select;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author rameses
 */
public class EnumItemsConverter implements Converter{
    
    /**
     * Creates a new instance of EnumItemsConverter
     */
    public EnumItemsConverter() {
    }
    
    public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if(value == null)
            return null;
        if(value.equals("null"))
            return null;
        UIComponent child = null;
        if(comp.getChildren().size() > 0)
            child = comp.getChildren().get(0);
        if(child == null){
            System.out.println(comp.getClass().getSimpleName()+ " Component does not have a child");
            return null;
        }
        Class classValue = child.getValueBinding("value").getType(ctx);
        return Enum.valueOf(classValue, value);
    }
    
    public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
        if(value == null)
            return null;
        if(value instanceof String)
            return value.toString();
        if(value instanceof Enum){
            return ((Enum) value).name();
        }
        return null;
    }
    
}
