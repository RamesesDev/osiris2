/*
 * CRUDLookup.java
 *
 * Created on January 26, 2010, 10:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client.ui.ext;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.common.LookupModel;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.common.UIView;
import java.util.ArrayList;
import java.util.List;

public abstract class SimpleLookup extends LookupModel {
    
    /** Creates a new instance of CRUDLookup */
    public SimpleLookup() {
    }
    
    public List getListActions() {
        return new ArrayList();
    }
    
    public List getPageActions() {
        List list = new ArrayList();
        list.add( new Action("handler.firstPage", null, "com/rameses/rcp/images/firstPage.png"));
        list.add( new Action("handler.backPage", null, "com/rameses/rcp/images/backPage.png"));
        list.add( new Action("handler.nextPage", null, "com/rameses/rcp/images/nextPage.png"));
        return list;
    }
    
    public List getLookupActions() {
        List list = new ArrayList();
        list.add( new Action("cancel", "Cancel", null));
        list.add( new Action("select", "OK", null));
        return list;
    }
    
    private Boolean hasQuery;
    
    //overridable
    public Opener getQueryHandler() {
        if(hasQuery==null) {
            hasQuery = false;
            for(UIView vw: controller.getViews()) {
                if(vw.getName()!=null && vw.getName().equalsIgnoreCase("query")) {
                    hasQuery = true;
                    break;
                }
            }
        }
        if(hasQuery==true) {
            Opener opener = new Opener();
            opener.setOutcome("query");
            return opener;
        }
        return null;
    }

    public Object onOpenItem(Object o, String columnName) {
        return select();
    }
    
}
