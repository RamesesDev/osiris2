/*
 * CRUDLookup.java
 *
 * Created on January 26, 2010, 10:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client.ui.ext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CRUDLookup extends SimpleLookup {
    
    private Object service;
    private String entityClass;
    
    public Map getQueryParams() {
        return null;
    }
    
    public Map getOptions() {
        return null;
    }

    public boolean isMapList() {
        return false;
    }
    
    public String getCondition() {
        return null;
    }
    
    public String getSortOrder() {
        return null;
    }
    
    public List fetchList(Map map) {
        String ql = null;
        if ( isMapList() )
            ql = CRUDUtil.getMapQL( getEntityClass(), getColumns(), getCondition(), getSortOrder() );
        else
            ql = CRUDUtil.getEntityQL( getEntityClass(), getColumns(), getCondition(), getSortOrder() );
        
        Map params = new HashMap();
        params.putAll( map );
        Map ext = getQueryParams();
        if ( ext != null )
            params.putAll( ext );
        
        return CRUDService.getList( getService(), ql, params, getOptions() );
    }
    
    public Object getService() {
        if ( service != null)
            return service;
        else
            return CRUDService.getService();
    }
    
    public void setService(Object service) {
        this.service = service;
    }
    
    public String getEntityClass() {
        return entityClass;
    }
    
    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }
    
    public CRUDLookup() {
    }
    
    
}
