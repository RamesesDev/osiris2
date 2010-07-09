/*
 * UIEnumList.java
 *
 * Created on May 13, 2009, 12:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.component.select;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author rameses
 */
public class UIEnumList extends UISelectItems{
    
    /** Creates a new instance of UIEnumList */
    public UIEnumList() {
    }
    
    
    
    public Object getValue() {
        Object parent = getParent();
        if(parent == null)
            return null;
        if(!(parent instanceof UISelectOne || parent instanceof UISelectMany)){
            return null;
        }
        List items = new ArrayList();
        SelectItem item;
        String _caption;
        UIInput selectComp = (UIInput) parent;
        selectComp.setConverter(new EnumItemsConverter());
        FacesContext ctx = FacesContext.getCurrentInstance();
        String caption = (String) getAttributes().get("caption");
        String emptyText = (String) getAttributes().get("emptyText");
        Class classValue = getValueBinding("value").getType(ctx);
        Enum[] source = (Enum[])classValue.getEnumConstants();
        ValueBinding vbCaption = null;
        if(caption != null)
            vbCaption = ctx.getApplication().createValueBinding("#{obj."+caption+"}");
        if(emptyText != null){
            item = new SelectItem("null", emptyText);
            items.add(item);
        }
        for(Enum obj : source){
            ((HttpServletRequest)ctx.getExternalContext().getRequest()).setAttribute("obj", obj);
            if(vbCaption != null){
                _caption = (String) vbCaption.getValue(ctx);                
            }
            else{
                _caption = obj.toString();
            }
            item = new SelectItem();
            item.setValue(obj.name());
            item.setLabel(_caption);
            items.add(item);
        }
        return items;
    }
}
