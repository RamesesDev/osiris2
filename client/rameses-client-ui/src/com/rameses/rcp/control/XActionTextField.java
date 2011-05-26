/*
 * XActionTextField.java
 *
 * Created on November 3, 2010, 10:48 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.common.MethodResolver;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;


public class XActionTextField extends AbstractIconedTextField {
    
    private String actionName;
    
    
    public XActionTextField() {
        super("com/rameses/rcp/icons/search.png");
        setOrientation( super.ICON_ON_RIGHT );
    }
    
    public void actionPerformed() {
        UIInputUtil.updateBeanValue(this);
        if ( ValueUtil.isEmpty(actionName) ) return;
        
        try {
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            Object outcome = mr.invoke(binding.getBean(), actionName, null, null);
            if ( outcome == null ) return;
            
            ControlSupport.fireNavigation(this, outcome);
            
        } catch(Exception ex){
            MsgBox.err(new RuntimeException(ex));
        }
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    
}
