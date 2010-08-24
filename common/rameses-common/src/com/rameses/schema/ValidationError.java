/*
 * ValidationError.java
 *
 * Created on August 12, 2010, 10:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

/**
 *
 * @author elmo
 */
public class ValidationError {
    
    private String code;
    private String message;
    
    public ValidationError(String c, String msg) {
        this.code = c;
        this.message = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    public String toString() {
        if(code==null)
            return message; 
        return message; 
    }
    
}
