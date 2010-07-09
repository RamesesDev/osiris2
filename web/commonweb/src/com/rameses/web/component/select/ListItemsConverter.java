/*
 * ListItemsConverter.java
 *
 * Created on May 11, 2009, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.component.select;

import java.util.Collection;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author rameses
 */
public class ListItemsConverter implements Converter{
    /**
     * Creates a new instance of ListItemsConverter
     */
    public ListItemsConverter() {
    }
    
    private String _value;
    
    public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
        UIComponent child = null;
        if(comp.getChildren().size() > 0)
            child = comp.getChildren().get(0);
        if(child == null){
            System.out.println(comp.getClass().getSimpleName()+ " Component does not have a child");
            return null;
        }        
        String key = (String) child.getAttributes().get("key");
        ValueBinding vbKey = null;
        if(key != null)
            vbKey = ctx.getApplication().createValueBinding("#{obj."+key+"}");
        Collection list = (Collection) child.getValueBinding("value").getValue(ctx);
        if(list == null) {
            System.out.println("list is null");
            return null;
        }
        String _key;
        for(Object obj : list){
            ((HttpServletRequest)ctx.getExternalContext().getRequest()).setAttribute("obj", obj);
            if(vbKey != null) 
                _key = (String) vbKey.getValue(ctx);
            else
                _key = obj.toString();
            if(_key.equals(value)){
                return obj;
            }
        }
        return null;
    }
    
    public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
        if(value == null)
            return null;
        if(value instanceof String)
            return value.toString();
        UIComponent child = null;
        if(comp.getChildren().size() > 0)
            child = comp.getChildren().get(0);
        if(child == null){
            System.out.println(comp.getClass().getSimpleName()+ " Component does not have a child");
            return null;
        }
        String key = (String) child.getAttributes().get("key");
        ((HttpServletRequest)ctx.getExternalContext().getRequest()).setAttribute("objvalue", value);
        ValueBinding vbKey = null;
        if(key != null)
            vbKey = ctx.getApplication().createValueBinding("#{objvalue."+key+"}");
        String _key;
        if(vbKey != null) _key = (String) vbKey.getValue(ctx);
        else
            _key = value.toString();
        return _key;
    }
}
