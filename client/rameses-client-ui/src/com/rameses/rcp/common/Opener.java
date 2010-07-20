package com.rameses.rcp.common;

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
        if ( id == null )
            return name;
        
        return name + "_" + id;
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
    
}
