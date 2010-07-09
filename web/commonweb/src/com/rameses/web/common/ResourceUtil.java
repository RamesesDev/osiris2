
package com.rameses.web.common;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class ResourceUtil {
    
    public static final String SCRIPTS_PARAM = ResourceUtil.class.getName().concat("SCRIPTS");
    public static final String STYLES_PARAM = ResourceUtil.class.getName().concat("STYLES");
    
    public static void addScriptResource(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        List resources = (List) req.getAttribute(SCRIPTS_PARAM);
        if (resources == null) {
            resources = new ArrayList();
            req.setAttribute(SCRIPTS_PARAM, resources);
        }
        
        name = context.getExternalContext().getRequestContextPath() + Filter.RESOURCE_KEY + "/" + name;
        if (resources.contains(name)) return;
        
        resources.add( name);
    }
    
    public static void addCSSResource(String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        List resources = (List) req.getAttribute(STYLES_PARAM);
        if (resources == null) {
            resources = new ArrayList();
            req.setAttribute(STYLES_PARAM, resources);
        }
        
        name = context.getExternalContext().getRequestContextPath() + Filter.RESOURCE_KEY + "/" + name;
        if (resources.contains(name)) return;
        
        resources.add( name);
    }
    
}
