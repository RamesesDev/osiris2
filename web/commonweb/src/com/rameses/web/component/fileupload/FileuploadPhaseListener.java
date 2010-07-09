
package com.rameses.web.component.fileupload;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;

public class FileuploadPhaseListener implements PhaseListener {
    
    public static final String ABORT_PHASE = "___ABORT_PHASE_4";
    
    public FileuploadPhaseListener() {
    }

    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext context = phaseEvent.getFacesContext();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        if (req.getAttribute(ABORT_PHASE) != null) {
            context.responseComplete();
        }
    }

    public void beforePhase(PhaseEvent phaseEvent) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.UPDATE_MODEL_VALUES;
    }
    
}
