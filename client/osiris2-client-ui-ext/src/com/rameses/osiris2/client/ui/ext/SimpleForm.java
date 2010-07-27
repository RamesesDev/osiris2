package com.rameses.osiris2.client.ui.ext;

import com.rameses.rcp.annotations.Close;
import com.rameses.rcp.annotations.Controller;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.framework.ChangeLog;
import com.rameses.rcp.framework.UIController;
import java.util.List;

public class SimpleForm {
    
    @Controller
    private UIController controller;
    
    @com.rameses.rcp.annotations.ChangeLog
    private ChangeLog changeLog; 
    
    public SimpleForm() {
    }
    
    public String getFormTitle() {
        return controller.getTitle();
    }
    
    public List getFormActions() {
        return CommonUtil.getCloseButton();
    }
    
    @Close
    public boolean checkChanges() {
        if( changeLog.hasChanges() ) {
            if( MsgBox.confirm("Changes have been made. Discard changes?") ) {
                changeLog.undoAll();
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    }
    
    
}
