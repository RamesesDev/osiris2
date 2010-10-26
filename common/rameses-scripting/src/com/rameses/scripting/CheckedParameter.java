/*
 * CheckedParameter.java
 *
 * Created on August 12, 2010, 9:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.io.Serializable;

/**
 *
 * @author elmo
 */
public class CheckedParameter implements Serializable {
    
    //validation schema or rule    
    private String schema;
    
    //position of the argument
    private int index;
    
    //requires that object must not be empty or null.
    private boolean required;
    
    //requires that object must not be empty or null.
    private String dataTypes;
    
    public CheckedParameter(String schema, int index, boolean required, String dt) {
        this.schema = schema;
        this.index = index;
        this.required = required;
        if(dt!=null && dt.trim().length()>0) {
            this.dataTypes = dt;
        }
    }

    public String getSchema() {
        return schema;
    }

    public int getIndex() {
        return index;
    }

    public boolean isRequired() {
        return required;
    }

    public String getDataTypes() {
        return dataTypes;
    }
    
}
