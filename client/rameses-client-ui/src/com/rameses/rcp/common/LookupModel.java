package com.rameses.rcp.common;

//import com.rameses.common.annotations.Controller;

/**
 * The lookup list model extends paging model
 * - returnSingleResult is when you want the lookup to
 *    immediately return a result without popping the lookup
 *    dialog. null null is the default
 */
public abstract class LookupModel extends PageListModel {
    
//    @Controller
//    protected UIController controller;
    
    private LookupSelector selector;
    
    
    public LookupModel() {
        super();
    }
    
    public String select() {
        if( getSelectedItem().getItem()!=null ) {
            Object o = getSelectedItem().getItem();
            if(selector!=null) {
                selector.select(o);
            }
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
    
    public void setSelector(LookupSelector s) {
        this.selector = s;
    }
    
    public boolean show(String t) {
        setSearch(t);
        load();
        
        if(getDataList().size()==0) {
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
    
    //overridable
    public boolean selectSingleResult() {
        return false;
    }
    
    public void destroy() {
        selector = null;
        super.destroy();
    }
    
    
}
