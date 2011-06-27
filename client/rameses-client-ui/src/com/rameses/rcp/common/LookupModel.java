package com.rameses.rcp.common;

import com.rameses.rcp.annotations.Controller;
import com.rameses.rcp.framework.UIController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import com.rameses.common.annotations.Controller;

/**
 * The lookup list model extends paging model
 * - returnSingleResult is when you want the lookup to
 *    immediately return a result without popping the lookup
 *    dialog. null null is the default
 */
public class LookupModel extends PageListModel {
    
    @Controller
    protected UIController controller;
    
    private LookupSelector selector;
    private List emptyList;
    
    
    public LookupModel() {
        super();
    }
    
    //overridable methods---
    public Object getSelectedValue() {
        return ( getSelectedItem().getItem()!=null ) ? 
               getSelectedItem().getItem() : null;
    }
    
    public List fetchList(Map o) {
        if( emptyList != null ) return emptyList;
        
        emptyList = new ArrayList();
        return emptyList;
    }
    
    public boolean selectSingleResult() {
        return false;
    }
    
    public boolean errorOnEmpty() {
        return false;
    }
    
    //default implementation for select and cancel
    public String select() {
        if(selector!=null) {
            selector.select( getSelectedValue() );
        }
        return "_close";
    }
    
    public String emptySelection() {
        if(selector!=null) {
            selector.select(null);
        }
        return "_close";
    }
    
    public String cancel() {
        if(selector!=null) {
            selector.cancelSelection();
        }
        return "_close";
    }
    
    //invoked when the lookup screen is shown
    public boolean show(String t) {
        setSearch(t);
        load();
        
        if(errorOnEmpty() && getDataList().size()==0) {
            throw new IllegalStateException("There are no records found");
        }
        
        if(selectSingleResult() && getDataList().size()==1) {
            Object retVal = getDataList().get(0);
            if(selector!=null) {
                selector.select(retVal);
            }
            return false;
        } else {
            return true;
        }
    }
    
    //for the screen
    public LookupModel getHandler() {
        return this;
    }
    
    public void destroy() {
        selector = null;
        super.destroy();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public LookupSelector getSelector() {
        return selector;
    }
    
    public void setSelector(LookupSelector s) {
        this.selector = s;
    }
    //</editor-fold>
    
}
