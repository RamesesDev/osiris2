package com.rameses.web.component.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author jaycverg
 */
public class UISelectItems extends javax.faces.component.UISelectItems {

    
    public Object getValue() {
        Object parent = getParent();
        
        if(parent == null) return null;
        if(!(parent instanceof UISelectOne || parent instanceof UISelectMany)){
            return null;
        }
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        UIInput selectComp = (UIInput) parent;
        selectComp.setConverter(new SelectItemsConverter());
        
        String keyAttr = (String) getAttributes().get("key");
        String captionAttr = (String) getAttributes().get("caption");
        String descriptionAttr = (String) getAttributes().get("description");
        String emptyTextAttr = (String) getAttributes().get("emptyText");
        ValueBinding valueVb = getValueBinding("value");
        Class clazz = valueVb.getType(ctx);
        Object value = valueVb.getValue(ctx);
        
        List items = new ArrayList();
        if(emptyTextAttr != null){
            SelectItem item = new SelectItem("null", emptyTextAttr);
            items.add(item);
        }
        
        if ( clazz.isEnum() ) {
            Enum []list = (Enum[]) clazz.getEnumConstants();
            for(Object item: list) {
                addItem(item, keyAttr, captionAttr, items);
            }
        } else if ( clazz.isArray() && value != null ) {
            for (Object item: (Object[]) value) {
                addItem(item, keyAttr, captionAttr, items);
            }
        } else if ( value instanceof Collection && value != null ) {
            for(Object item : (Collection) value){
                addItem(item, keyAttr, captionAttr, items);
            }
        }
        return items;
    }
    
    private void addItem(Object value, String keyAttr, String captionAttr, List items) {
        String key = getProperty(value, keyAttr);
        String caption = getProperty(value, captionAttr);
        SelectItem item = new SelectItem(key, caption);
        items.add(item);
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
