/*
 * Opener.java
 *
 * Created on May 26, 2010, 1:46 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Opener implements Serializable {
    
    private String name;
    private String action;
    private String outcome;
    private Map params = new HashMap();
    
    public Opener() {
    }
    
    public int hashCode() {
        return (getClass().getName() + name).hashCode();
    }
    
    public boolean equals(Object obj) {
        return obj != null && this.hashCode() == obj.hashCode();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setModuleName(String moduleName) {
        if ( !name.contains(":") )
            name = moduleName + ":" + name;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getOutcome() {
        return outcome;
    }
    
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
    
    public Map getParams() {
        return params;
    }
    
    public void setParams(Map params) {
        this.params = params;
    }
    
    public String getViewId() {
        String[] arr = name.split(":");
        StringBuffer sb = new StringBuffer();
        sb.append("/" + arr[0]);
        sb.append("/" + arr[1]);
        if ( action != null ) {
            sb.append("/" + action);
        }
        else if ( outcome != null ) {
            sb.append("/" + outcome);
        }
        sb.append(WebContext.PAGE_SUFFIX);
        
        return sb.toString();
    }
    
}
