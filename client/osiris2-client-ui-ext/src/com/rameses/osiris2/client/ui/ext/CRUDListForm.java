/*
 * CRUDListForm.java
 *
 * Created on January 25, 2010, 11:38 AM
 * @author jaycverg
 */

package com.rameses.osiris2.client.ui.ext;

import com.rameses.rcp.annotations.Close;

public abstract class CRUDListForm extends CRUDForm {
    
    private CRUDList listHandler;
    private String listId;
    
    
    public final Object getListHandler() {
        //flag used for closing list view
        if ( listId == null)
            listId = controller.getId();
        
        if ( listHandler == null ) {
            listHandler = (CRUDList) createListHandler();
            listHandler.setController( controller );
            listHandler.setService( getService() );
            listHandler.setEntityClass( getEntityClass() );
        }
        return listHandler;
    }
    
    public abstract Object createListHandler();
    
    @Close
    public boolean closeForm() {
        if ( listId != null && listId.equals(controller.getId())  ) {
            try {
                changeLog.clear();
            } catch (Exception e) {;}
            
            return true;
        }
        
        return super.closeForm();
    }
    
}
