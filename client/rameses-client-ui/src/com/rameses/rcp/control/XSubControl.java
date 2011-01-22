/*
 * XSubControl.java
 *
 * Created on January 22, 2011, 2:09 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;


public class XSubControl extends XSubFormPanel {
    
    private AbstractSubControlModel model;
    
    public XSubControl() {
        super();
    }
    
    public void load() {
        super.load();
        model.onInit();
    }
    
    public void refresh() {
        model.onRefresh();
        super.refresh();
    }
    
}
