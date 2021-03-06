/*
 * BasicPageList.java
 *
 * Created on January 24, 2010, 6:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client.ui.ext;

import com.rameses.rcp.annotations.Controller;
import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.common.PageListModel;
import com.rameses.rcp.framework.UIController;
import java.util.ArrayList;
import java.util.List;

public abstract class ListViewController extends PageListModel {
    
    //this is only applicable if this is used as the top form,
    //else use the setController method to set this.
    @Controller
    private UIController controller;
    
    public UIController getController() {
        return controller;
    }
    
    public ListViewController() {
    }

    public String getTitle() {
        return controller.getTitle();
    }
    
    public AbstractListModel getListHandler() {
        return this;
    }
    
    public List getPageActions() {
        return CommonUtil.getPageActions();
    }
    
    public List getListActions() {
        return CommonUtil.getListActions(controller.getName());
    }
    
    public List getViewActions() {
        return new ArrayList();
    }
    
    public Opener getQueryHandler() {
        //check first if a query name exists in the ListView
        if(CommonUtil.isViewExist(controller, "query") ) {
            Opener query =  new Opener();
            query.setOutcome("query");
            return query;
        }
        return null;
    }

    public Object create() {
        System.out.println("Override create method");
        return null;
    }
    
    public Object open() {
        System.out.println("Override open method");
        return null;
    }

    public void setController(UIController controller) {
        this.controller = controller;
    }

    public Object onOpenItem(Object o, String columnName) {
        return open();
    }
    
}
