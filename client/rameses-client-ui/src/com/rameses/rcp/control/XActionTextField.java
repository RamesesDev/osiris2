/*
 * XActionTextField.java
 *
 * Created on November 3, 2010, 10:48 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.common.MethodResolver;
import com.rameses.rcp.common.Action;
import com.rameses.rcp.common.LookupModel;
import com.rameses.rcp.common.LookupSelector;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;


public class XActionTextField extends AbstractIconedTextField implements LookupSelector {
    
    private String actionName;
    private Object actionObject;
    
    
    public XActionTextField() {
        super("com/rameses/rcp/icons/search.png");
        setOrientation( super.ICON_ON_RIGHT );
    }
    
    public void actionPerformed() {
        UIInputUtil.updateBeanValue(this);
        if ( ValueUtil.isEmpty(actionName) && actionObject == null ) return;
        
        try {
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            Object outcome = null;
            
            if( !ValueUtil.isEmpty(actionName) )
                outcome = mr.invoke(binding.getBean(), actionName, null, null);
            else if ( actionObject instanceof Action )
                outcome = ((Action)actionObject).execute();
            else
                outcome = actionObject;
                
            if ( outcome == null ) return;
            
            if ( outcome instanceof Opener ) {
                Opener opener = (Opener) outcome;
                ControlSupport.initOpener(opener, binding.getController());
                Object bean = opener.getController().getCodeBean();
                if( bean instanceof LookupModel ) {
                    LookupModel lm = (LookupModel) bean;
                    lm.setSelector(this);
                    Object value = getValue();
                    lm.setSearch( value==null? null : value.toString() );
                }
            }
            
            binding.fireNavigation(outcome);
            
        } catch(Exception ex){
            MsgBox.err(new RuntimeException(ex));
        }
    }
    
    public void select(Object o) {
        setText( o == null? "" : o.toString() );
        UIInputUtil.updateBeanValue(this);
    }

    public void cancelSelection() {
    }
    
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Object getActionObject() {
        return actionObject;
    }

    public void setActionObject(Object actionObject) {
        this.actionObject = actionObject;
    }

}
