/*
 * FormControl.java
 *
 * Created on October 18, 2010, 11:51 AM
 * @author jaycverg
 */

package com.rameses.rcp.common;

import java.util.HashMap;
import java.util.Map;


public class FormControl {
    
    private static final long serialVersionUID = 1L;
    
    
    private String type;
    private Map properties = new HashMap();
    
    
    public FormControl() {
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public Map getProperties() {
        return properties;
    }
    
    public void setProperties(Map properties) {
        this.properties = properties;
    }
    
}
