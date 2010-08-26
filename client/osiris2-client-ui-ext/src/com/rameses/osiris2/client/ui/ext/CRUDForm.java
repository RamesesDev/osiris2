/*
 * CRUDForm.java
 *
 * Created on January 25, 2010, 11:17 AM
 * @author jaycverg
 */

package com.rameses.osiris2.client.ui.ext;

import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.client.OsirisContext;
import com.rameses.rcp.framework.ChangeLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CRUDForm extends FormViewController {
    
    private Object service;
    
    public CRUDForm() {
    }
    
    public String getEntityClass(){ return ""; }
    public Object getEntityId(){ return null; }
    
    public String getUserName() {
        SessionContext session = OsirisContext.getSession();
        return (String) session.getEnv().get("user.name");
    }
    
    public Map getOptions() {
        return null;
    }
    
    public List getDetailLogs() {
        return null;
    }
    
    public Object createEntity() {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(getEntityClass()).newInstance();
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void view() {} //dummy method
    
    public String open() {
        Object o = CRUDService.read( getService(), getEntityClass(), getEntityId(), getOptions() );
        setEntity( o );
        return super.open();
    }
    
    public void saveNew() {
        Map options = getOptions();
        if ( options == null ) options = new HashMap();
        options.put("log", CRUDService.createLog(getUserName(), getEntityClass(), getEntityId(), "CREATE", null));
        Object entity = CRUDService.create( getService(), getEntityClass(), getEntityId(), getEntity(), options );
        setEntity( entity );
    }
    
    public void saveUpdate() {
        Map options = getOptions();
        List detailLogs = getDetailLogs();
        if ( changeLog.hasChanges() || hasChangesInDetailLogs() ) {
            if ( options == null ) options = new HashMap();
            options.put("log", CRUDService.createLog(getUserName(), getEntityClass(), getEntityId(), "UPDATE", changeLog, getDetailLogs()));
        }
        Object entity = CRUDService.update( getService(), getEntityClass(), getEntityId(), getEntity(), options );
        setEntity( entity );
        
    }
    
    public String delete() {
        Map options = getOptions();
        if ( options == null ) options = new HashMap();
        options.put("log", CRUDService.createLog(getUserName(), getEntityClass(), getEntityId(), "DELETE", null));
        CRUDService.delete( getService(), getEntityClass(), getEntityId(), getEntity(), options );
        return "_close";
    }
    
    public Object getService() {
        if ( service == null ) {
            return CRUDService.getService();
        }
        return service;
    }
    
    public void setService(Object service) {
        this.service = service;
    }
    
    //========= helper methods =========
    private boolean hasChangesInDetailLogs() {
        List logs = getDetailLogs();
        if (logs == null || logs.size() == 0)
            return false;
        
        for ( Object o : logs) {
            ChangeLog log = (ChangeLog) o;
            if ( log.hasChanges() ) return true;
        }
        
        return false;
    }
    
    
}
