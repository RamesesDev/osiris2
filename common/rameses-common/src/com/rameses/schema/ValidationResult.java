/*
 * ValidationErrors.java
 *
 * Created on August 12, 2010, 10:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import com.rameses.util.ValueUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmo
 */
public class ValidationResult {
    
    private String contextName;
    
    private List<ValidationError> errs = new ArrayList();
    
    private List<ValidationResult> subValidations = new ArrayList();
    
    public ValidationResult() {
    }
    
    
    
    public void addError(String code, String message) {
        errs.add( new ValidationError(code,message) );
    }
    
    public void addSubValidation( ValidationResult r  ) {
        subValidations.add( r );
    }
    
    public boolean hasErrors() {
        return (errs.size() + subValidations.size()) > 0;
    }

    void write( StringBuffer sb, int indent ) {
        String idnt = ValueUtil.repeat(null, indent); 
        for( ValidationError ve: errs ) {
            sb.append( idnt + ve.toString() + "\n");
        }
        for(ValidationResult vr: subValidations) {
            if( vr.getContextName()!=null) {
                sb.append( idnt + vr.getContextName() + "{ \n" );
                vr.write( sb, indent+1);
                sb.append( idnt +"} \n");
            }
            else {
                vr.write( sb, indent+1);
            }
        }
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        write(sb, 0);
        return sb.toString();
    }
    
    public List<ValidationError> getErrors() {
        return errs;
    }
    
    public List getSubValidations() {
        return subValidations;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }
    
}
