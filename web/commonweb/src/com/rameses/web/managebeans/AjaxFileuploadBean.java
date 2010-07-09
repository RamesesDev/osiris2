
package com.rameses.web.managebeans;

import com.rameses.web.common.ProgressHandler;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class AjaxFileuploadBean implements ActionListener {
    
    private Map progressHandlerManager = new ProgressHandlerManager();
    private Map attributes = new HashMap();
    
    public AjaxFileuploadBean() {
    }
    
    public Map getProgressHandler() {
        return progressHandlerManager;
    }
    
    //used for removing the progress handler
    public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
        UIComponent comp = actionEvent.getComponent();
        String progressId = (String) comp.getAttributes().get("progressId"); 
        progressHandlerManager.remove(progressId);
    }

    public Map getAttributes() {
        return attributes;
    }

    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }
    
}
