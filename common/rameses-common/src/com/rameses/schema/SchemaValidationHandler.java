/*
 * SchemaValidator2.java
 *
 * Created on August 14, 2010, 5:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import com.sun.jmx.remote.util.Service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author elmo
 */
public class SchemaValidationHandler implements SchemaHandler {
    
    private ValidationResult vr;
    private Stack stack = new Stack();
    private List<SimpleFieldValidator> fieldValidators; 
    private SchemaHandlerStatus status;
    
    public SchemaValidationHandler() {
    }
    
    public void startSchema(Schema schema) {
        vr = new ValidationResult();
    }

    public void setStatus(SchemaHandlerStatus status) {
        this.status = status;
    }

    public void startElement(SchemaElement element, Object data) {
    }

    public void processField(SimpleField f, String refname, Object value) {
        Class clazz = null;
        if( value!=null ) clazz = value.getClass();
        boolean passRequired = SchemaUtil.checkRequired(f,clazz,value);
        if( !passRequired ) {
            vr.addError("", refname + " is required ");
            return;
        }
            
        if( clazz !=null ) {
            boolean passType = SchemaUtil.checkType( f, clazz );
            if(!passType) {
                vr.addError( "", refname + " must be of type " + f.getType() );
                return;
            }
        }
        
        //load validators if it is not yet loaded.
        if( fieldValidators==null) {
            fieldValidators = new ArrayList();
            Iterator iter = Service.providers(SimpleFieldValidator.class, Thread.currentThread().getContextClassLoader());
            while(iter.hasNext()) {
                fieldValidators.add( (SimpleFieldValidator) iter.next());
            }
        }
        
        for(SimpleFieldValidator cfv: fieldValidators) {
            cfv.validate(vr,f,clazz,refname,value);
        }
    }

    public void endElement(SchemaElement element) {
    }

    public void startLinkField(LinkField f) {;}
    public void endLinkField(LinkField f) {;}
    public void startComplexField(ComplexField cf) {;}
    public void endComplexField(ComplexField cf) {;}
    public void endSchema(Schema schema) {;}

    public ValidationResult getResult() {
        return vr;
    }

   
    
}
