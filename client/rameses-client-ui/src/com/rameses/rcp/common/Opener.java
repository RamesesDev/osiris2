package com.rameses.rcp.common;

import com.rameses.rcp.framework.UIController;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jaycverg
 */
public class Opener {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String caption;
    private String name;
    private String action;
    private Map params = new HashMap();
    private String target;
    private String outcome;
    private String permission;
    private boolean modal = true;
    
    private UIController controller;
    private UIController caller;
    
    private Map properties = new HashMap();
    private OpenerLoader loader;
    
    public Opener() {
    }
    
    public Opener(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getId() {
        if( id == null ) {
            StringBuffer sb = new StringBuffer();
            if ( name != null )    sb.append(name);
            if ( caption != null ) sb.append(caption);
            return sb.toString();
        }
        
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public Map getParams() {
        return params;
    }
    
    public void setParams(Map params) {
        this.params = params;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public String getOutcome() {
        return outcome;
    }
    
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
    
    public UIController getController() {
        return controller;
    }
    
    public void setController(UIController controller) {
        this.controller = controller;
    }
    
    /**
     *  Shorthand for getting the controllers codeBean.
     *  Returns null if the controller is null.
     */
    public Object getHandle() {
       if( controller == null && loader!=null) {
            loader.load(this);
       }
       if ( controller == null ) return null;
       return controller.getCodeBean();
    }
    
    public UIController getCaller() {
        return caller;
    }
    
    public void setCaller(UIController caller) {
        this.caller = caller;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    public boolean isModal() {
        return modal;
    }
    
    public void setModal(boolean modal) {
        this.modal = modal;
    }
    
    public Map getProperties() {
        return properties;
    }
    
    public OpenerLoader getLoader() {
        return loader;
    }

    public void setLoader(OpenerLoader loader) {
        this.loader = loader;
    }
    
    public static interface OpenerLoader {
        void load( Opener o );
    }

   
    
}
