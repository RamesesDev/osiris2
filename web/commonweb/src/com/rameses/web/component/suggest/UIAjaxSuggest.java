
package com.rameses.web.component.suggest;

import com.rameses.web.common.ResourceUtil;
import java.io.IOException;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class UIAjaxSuggest extends UICommand {
    
    
    public UIAjaxSuggest() {
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        ResourceUtil.addScriptResource("js/jquery.js");
        ResourceUtil.addScriptResource("js/suggest.js");
        ResourceUtil.addScriptResource("js/rameses.web.js");
        ResourceUtil.addCSSResource("css/suggest.css");
        
        ResponseWriter writer = context.getResponseWriter();
        
        UIComponent parent = getParent();
        String parentId = parent.getClientId(context);
        String methodBinding = getAction().getExpressionString();
        
        StringBuffer script = new StringBuffer();
        
        script.append("$(document).ready(function() {");
        script.append("AjaxUtil.makeAutocomplete('" + parentId + "',");
        script.append("'" + methodBinding + "', '');");
        script.append("});");
        
        writer.startElement("script", this);
        writer.write(script.toString());
        writer.endElement("script");
    }
    
}
