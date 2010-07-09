package com.rameses.osiris2.client;

import com.rameses.rcp.framework.UIController;
import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.WorkUnitInstance;
import com.rameses.rcp.framework.ControllerProvider;

public class OsirisUIControllerProvider implements ControllerProvider {
    
    public UIController getController(String name) {
        SessionContext app = OsirisContext.getSession();
        WorkUnitInstance wi = app.getWorkUnit(name).newInstance();
        wi.setId(name);
        return new WorkUnitUIController( wi );
    }
    
    
}
