
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
public class UIBeanList extends UISelectItems{
    
    public UIBeanList(){
        
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
        String _key;
        String _caption;
        String _description;
        UIInput selectComp = (UIInput) parent;
        selectComp.setConverter(new ListItemsConverter());
        String key = (String) getAttributes().get("key");
        String caption = (String) getAttributes().get("caption");
        String description = (String) getAttributes().get("description");
        String emptyText = (String) getAttributes().get("emptyText");        
        FacesContext ctx = FacesContext.getCurrentInstance();
        ValueBinding vbKey = null;
        ValueBinding vbCaption = null;
        ValueBinding vbDescription = null;
        if(key != null)
            vbKey = ctx.getApplication().createValueBinding("#{obj."+key+"}");
        if(caption != null)
            vbCaption = ctx.getApplication().createValueBinding("#{obj."+caption+"}");
        if(description != null)
            vbDescription = ctx.getApplication().createValueBinding("#{obj."+description+"}");
        if(emptyText != null){
            item = new SelectItem("null", emptyText);
            items.add(item);
        }
        List list = (List) getValueBinding("value").getValue(ctx);        
        for(Object obj : list){
            ((HttpServletRequest)ctx.getExternalContext().getRequest()).setAttribute("obj", obj);
            _key = (String) ( (vbKey!=null) ? vbKey.getValue(ctx) : obj.toString() );
            _caption = (String) ( (vbCaption != null) ? vbCaption.getValue(ctx) : obj.toString() );
            _description = (String) ( (vbDescription != null) ? vbDescription.getValue(ctx) : obj.toString() );                            
            
            item = new SelectItem(_key, _caption);
            if(vbDescription != null)
                item.setDescription(_description);
            items.add(item);
        }
        return items;
    }
    
}

