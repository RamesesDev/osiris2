/*
 * MatchPatternFieldValidator.java
 *
 * Created on August 26, 2010, 1:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

/**
 *
 * @author elmo
 */
public class MatchPatternFieldValidator implements SimpleFieldValidator {

    public void validate(ValidationResult result, SimpleField field, Class fieldClass, String refname, Object fieldValue) {
        String pattern = (String)field.getProperties().get("pattern");
        if( (pattern!=null) && (pattern.trim().length()>0) &&  (fieldValue != null)  && (fieldValue instanceof String)) {
            String v = (String)fieldValue;
            if( !v.matches(pattern) ) {
                result.addError("", refname + " must match '" + pattern  + "'");
            }
        }
    }
    
}
