/*
 * CRUDList.java
 *
 * Created on January 25, 2010, 11:17 AM
 * @author jaycverg
 */

package com.rameses.osiris2.client.ui.ext;

import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.PropertyResolver;
import com.rameses.util.ValueUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CRUDList extends ListViewController {
    
    private Object service;
    private String entityClass;
    
    public CRUDList() {
    }
    
    public Map getQueryParams() {
        return null;
    }
    
    public Map getOptions() {
        return null;
    }
    
    public boolean isMapList() {
        return true;
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
        if ( service == null )
            return CRUDService.getService();
        
        return service;
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
    
    protected Object getPrimary(ListItem item) {
        String keyField = null;
        for(Column c: getColumns()) {
            if(c.isPrimary()) {
                keyField = c.getName();
                break;
            }
        }
        if(keyField!=null) {
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            return res.getProperty(item, keyField);
        }
        return null;
    }
    
    private Opener createOpener(String target, Object id, String controllerName) {
        Opener opener = new Opener();
        if ( ValueUtil.isEmpty( controllerName )) {
            controllerName = getController().getName();
        }
        
        String _id = controllerName;
        if( id !=null ) {
            _id = _id + "_" + id;
        }
        opener.setName( controllerName );
        opener.setId( _id );
        
        opener.setTarget(target);
        return opener;
    }
    
    public void view() { } //dummy method
    
    public String getOpenTitle() {
        return getPrimary( getSelectedItem() )+"";
    }
    
    public String getCreateTitle() {
        return "New " + getController().getName();
    }
    
    public Object open() {
        Object entity = getSelectedItem().getItem();
        if( entity == null) return null;
        String type = getOpenerType();
        if ( type == null )
            return "form";
        Object id = getPrimary(getSelectedItem());
        Opener opener = createOpener(type, id, getOpenItemController());
        opener.setAction("open");
        opener.getParams().put("entity", entity );
        opener.setCaption( getOpenTitle() );
        return opener;
    }
    
    public Object create() {
        String type = getOpenerType();
        if ( type == null )
            return "form";
        
        Opener opener = createOpener(type, "new", getCreateItemController());
        opener.setAction("create");
        opener.setCaption( getCreateTitle() );
        return opener;
    }
    
    
    public String getOpenerType() {
        return "_window";
    }
    
    public String getOpenItemController() {
        return null;
    }
    
    public String getCreateItemController() {
        return null;
    }
}
