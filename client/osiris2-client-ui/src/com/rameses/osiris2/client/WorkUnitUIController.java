package com.rameses.osiris2.client;

import com.rameses.classutils.ClassDefUtil;
import com.rameses.rcp.framework.UIController;
import com.rameses.osiris2.Page;
import com.rameses.osiris2.WorkUnitInstance;
import com.rameses.rcp.annotations.FormId;
import com.rameses.rcp.annotations.FormTitle;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.util.ControlSupport;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkUnitUIController extends UIController 
{
    
    private WorkUnitInstance workunit;
    private String id;
    private String title;
    private String name;
    private String defaultPageName = "default";
    
    private String formId;
    private String formTitle;
    
    public WorkUnitUIController(WorkUnitInstance wu) {
        this.workunit = wu;
        this.id = wu.getId();
        this.name = wu.getId();
        this.title = wu.getTitle();
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
    
    
    public void setId( String id) {
        this.id = id;
    }
    
    public void setTitle( String title) {
        this.title = title;
    }
    
    public String getId() {
        if(formId==null) {
            Object codeBean = getCodeBean();
            try {
                Field fld = ClassDefUtil.getInstance().findAnnotatedField( codeBean.getClass(), FormId.class  );
                if(fld!=null)formId = (String)ClientContext.getCurrentContext().getPropertyResolver().getProperty(codeBean, fld.getName());
            } catch(Exception ign){;}
            
            //check for methods
            if(formId==null) {
                try {
                    Method m = ClassDefUtil.getInstance().findAnnotatedMethod( codeBean.getClass(), FormId.class  );
                    if(m!=null) {
                        formId = (String)ClientContext.getCurrentContext().getMethodResolver().invoke(codeBean,m.getName(),null,null);
                    }
                } catch(Exception ign){;}
            }
            if(formId==null) formId = "";
        }
        if(formId.trim().length()>0)
            return formId;
        else
            return id;
    }
    
    public String getTitle() {
        if(formTitle==null) {
            Object codeBean = getCodeBean();
            //check for fields
            try {
                Field fld = ClassDefUtil.getInstance().findAnnotatedField( codeBean.getClass(), FormTitle.class  );
                if(fld!=null)formTitle = (String)ClientContext.getCurrentContext().getPropertyResolver().getProperty(codeBean, fld.getName());
            } catch(Exception ign){;}
            
            //check for methods
            if(formTitle==null) {
                try {
                    Method m = ClassDefUtil.getInstance().findAnnotatedMethod( codeBean.getClass(), FormTitle.class  );
                    if(m!=null) {
                        formTitle = (String)ClientContext.getCurrentContext().getMethodResolver().invoke(codeBean,m.getName(),null,null);
                    }
                } catch(Exception ign){;}
            }
            if(formTitle==null) formTitle = "";
        }
        if(formTitle.trim().length()>0)
            return formTitle;
        else
            return title;
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
