/*
 * CustomFieldValidator.java
 *
 * Created on August 12, 2010, 2:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

/**
 *
 * @author elmo
 */
public interface SimpleFieldValidator {
    void validate(ValidationResult result, SimpleField field, Class fieldClass, Object fieldValue );
}
