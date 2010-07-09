
package com.rameses.web.component.resource;

import com.rameses.web.common.ResourceUtil;
import java.io.IOException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class UIJQueryResource extends UIComponentBase{
    
    public static final String COMPONENT_FAMILY = UIJQueryResource.class.getName();
    public static final String JQUERY_RESOURCE = UIJQueryResource.class.getName();
    
    @Override
    public String getFamily() {
        return UIJQueryResource.COMPONENT_FAMILY;
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        String name = (String) getAttributes().get("name");
        if (name.trim().endsWith(".js")) {
            ResourceUtil.addScriptResource(name);
        }
        else if (name.trim().endsWith(".css")) {
            ResourceUtil.addCSSResource(name);
        }
    }
}
