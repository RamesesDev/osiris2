/*
 * Parameter.java
 *
 * Created on December 28, 2009, 6:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class Parameter {
    
    private String name;
    private DataType type;
    private Map properties = new HashMap();
    
    public Parameter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public Map getProperties() {
        return properties;
    }
    
}
