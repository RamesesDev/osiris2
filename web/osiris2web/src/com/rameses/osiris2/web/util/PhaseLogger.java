package com.rameses.osiris2.web.util;
import com.rameses.osiris2.web.*;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author elmo
 */

public class PhaseLogger implements PhaseListener {
    
    
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
    
    public void beforePhase(PhaseEvent event) {
        System.out.println("after phase " + event.getPhaseId());
        System.out.println("wi is " + WebContext.getInstance().getCurrentWorkUnitInstance());
    }
    
    public void afterPhase(PhaseEvent event) {
        System.out.println("after phase " + event.getPhaseId());
        System.out.println("wi is " + WebContext.getInstance().getCurrentWorkUnitInstance());
    }
    
}



