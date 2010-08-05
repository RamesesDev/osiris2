package com.rameses.osiris2.client;

import com.rameses.rcp.framework.UIController;
import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.Page;
import com.rameses.osiris2.WorkUnitInstance;
import com.rameses.rcp.framework.ControlSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkUnitUIController extends UIController {
    
    private WorkUnitInstance workunit;
    private String id;
    private String title;
    private String name;
    private String defaultPageName = "default";
    
    public WorkUnitUIController(WorkUnitInstance wu) {
        this.workunit = wu;
        this.id = workunit.getId();
        this.title = workunit.getTitle();
    }
    
    public Object getCodeBean() {
        return workunit.getController();
    }
    
    public String getDefaultView() {
        if(defaultPageName==null) {
            if( workunit.getWorkunit().getPages().size()>0 ) {
                defaultPageName = workunit.getCurrentPage().getName();
            }
        }
        return defaultPageName;
    }
    
    public UIController.View[] getViews() {
        List<UIController.View> list = new ArrayList();
        for(Object o: workunit.getWorkunit().getPages().values()) {
            Page p = (Page)o;
            list.add(new UIController.View( p.getName(), p.getTemplate() ) );
        }
        return (UIController.View[]) list.toArray(new UIController.View[]{});
    }
    
    public String getId() {
        return id;
    }
    
    public void setId( String id) {
        this.id = id;
    }
    
    public void setTitle( String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    private Class pageToClass(String name) {
        try {
            SessionContext app = OsirisContext.getSession();
            return app.getClassLoader().loadClass(name);
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Object init(Map properties, String action) {
        //set the properties
        ControlSupport.setProperties( getCodeBean(), properties );
        
        //check first if the workunit has already started.
        //if it has already started, fire the transition.
        //The subprocess normnally calls _close:signalAction.
        if( workunit.getPageFlow()!=null && workunit.isStarted()) {
            if(action ==null) {
                workunit.signal();
            } else {
                workunit.signal(action);
            }
            
            //determine which page to display next.
            if( workunit.isPageFlowCompleted() )
                return "_close";
            else
                return workunit.getCurrentPage().getName();
        } else {
            if( workunit.getWorkunit().getPages().size()>0 ) {
                defaultPageName = workunit.getCurrentPage().getName();
            }
            if(action == null ) {
                return null;
            } else if( action.startsWith("_")) {
                return action.substring(1);
            } else {
                return ControlSupport.invoke(  getCodeBean(), action, null);
            }
            
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public WorkUnitInstance getWorkunit() {
        return workunit;
    }
    
    
    
}
