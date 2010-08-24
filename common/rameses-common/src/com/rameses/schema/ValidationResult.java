/*
 * ValidationErrors.java
 *
 * Created on August 12, 2010, 10:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmo
 */
public class ValidationResult {
    
    private List<ValidationError> errs = new ArrayList();
    
    public ValidationResult() {
    }
    
    public void addError(String code, String message) {
        errs.add( new ValidationError(code,message) );
    }
    
    public boolean hasErrors() {
        return errs.size() > 0;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for( ValidationError ve: errs ) {
            sb.append( ve.toString() + "\n");
        }
        return sb.toString();
    }
    
}
