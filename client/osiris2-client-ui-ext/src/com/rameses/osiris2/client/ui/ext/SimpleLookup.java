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
import com.rameses.rcp.framework.UIController.View;
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
        list.add( new Action("handler.moveFirstPage", null, "com/rameses/osiris2/images/arrowup.gif"));
        list.add( new Action("handler.moveBackPage", null, "com/rameses/osiris2/images/arrowleft.gif"));
        list.add( new Action("handler.moveNextPage", null, "com/rameses/osiris2/images/arrowright.gif"));
        return list;
    }
        
    private Boolean hasQuery;
    
    //overridable
    public Opener getQueryHandler() {
        if(hasQuery==null) {
            hasQuery = false;
            for( View vw: controller.getViews() ) {
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
