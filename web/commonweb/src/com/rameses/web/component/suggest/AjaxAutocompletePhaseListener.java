
package com.rameses.web.component.suggest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

public class AjaxAutocompletePhaseListener implements PhaseListener{
    
    public void beforePhase(PhaseEvent event) {
        //No-op
    }
    
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        if (requestMap.get("autocomplete") != null) {
            Application app = context.getApplication();
            String searchKey = (String) requestMap.get("q");
            String suggestMethod = (String) requestMap.get("method");
            
            Collection<String> result = new ArrayList();
            //invoke the auto complete method
            try {
                MethodBinding source = app.createMethodBinding(suggestMethod, new Class[]{String.class});
                result = (Collection) source.invoke(context, new Object[]{ searchKey });
            } catch(Exception e) {;}
            
            StringBuffer resultText = new StringBuffer("");
            for (String s : result) {
                resultText.append(s + "\n");
            }
            
            HttpServletResponse resp = (HttpServletResponse) context.getExternalContext().getResponse();
            resp.setContentType("text/plain");
            resp.setStatus(200);
            try {
                resp.getWriter().write(resultText.toString());
                resp.getWriter().flush();
                resp.getWriter().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            context.responseComplete();
        }
    }
    
}
