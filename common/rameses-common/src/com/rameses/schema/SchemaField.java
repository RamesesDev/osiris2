/*
 * SchemaField.java
 *
 * Created on August 12, 2010, 10:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elmo
 */
public abstract class SchemaField implements Serializable{
    
    private SchemaElement element;
    private Map properties = new HashMap();
    
    public SchemaField() {
    }
    
    public Map getProperties() {
        return properties;
    }

    public abstract String getName();
    public abstract boolean isRequired();

    public SchemaElement getElement() {
        return element;
    }

    //can be set only by the parent.
    void setElement(SchemaElement element) {
        this.element = element;
    }
    
}
