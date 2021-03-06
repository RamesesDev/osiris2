
package com.rameses.web.common;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import org.ajax4jsf.framework.ajax.AjaxContext;


public class ResourceUtilPhaseListener implements PhaseListener {
    
    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        List<String> scripts = (List<String>) req.getAttribute(ResourceUtil.SCRIPTS_PARAM);
        if (scripts != null) {
            Set a4jscripts = (Set) req.getAttribute(AjaxContext.SCRIPTS_PARAMETER);
            if (a4jscripts == null) {
                a4jscripts = new LinkedHashSet();
                req.setAttribute(AjaxContext.SCRIPTS_PARAMETER, a4jscripts);
            }
            for (String resource : scripts)
                a4jscripts.add(resource);
        }
        
        List<String> styles = (List<String>) req.getAttribute(ResourceUtil.STYLES_PARAM);
        if (styles != null) {
            Set a4jstyles = (Set) req.getAttribute(AjaxContext.STYLES_PARAMETER);
            if (a4jstyles == null) {
                a4jstyles = new LinkedHashSet();
                req.setAttribute(AjaxContext.STYLES_PARAMETER, a4jstyles);
            }
            for (String resource : styles)
                a4jstyles.add(resource);
        }
    }
    
    public void beforePhase(PhaseEvent phaseEvent) {
    }
    
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
    
    
}
