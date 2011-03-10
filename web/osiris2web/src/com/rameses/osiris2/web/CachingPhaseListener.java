/*
 * CachingPhaseListener.java
 *
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author elmo
 */
public class CachingPhaseListener implements PhaseListener {
    
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
    
    public void afterPhase(PhaseEvent event) {}
    
    public void beforePhase(PhaseEvent event) {
        HttpServletResponse response = WebContext.getInstance().getResponse();
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Expires", "0");
    }
    
}
