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
import java.util.Arrays;
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
    private Schema schema;
    
    public SchemaValidationHandler() {
    }
    
    public void startSchema(Schema schema) {
        this.schema = schema;
        vr = new ValidationResult();
    }
    
    public void setStatus(SchemaHandlerStatus status) {
        this.status = status;
    }
    
    public void startElement(SchemaElement element, Object data) {
    }
    
    public void processField(SimpleField f, String refname, Object value) {
        boolean passRequired = SchemaUtil.checkRequired(f,value);
        if( !passRequired ) {
            vr.addError("", refname + " is required ");
            return;
        }
        
        Class clazz = null;
        if( value!=null ) clazz = value.getClass();
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
    
    public void startComplexField(ComplexField cf,String refname,SchemaElement element,Object data) {
        boolean passRequired = SchemaUtil.checkRequired(cf,data);
        if( !passRequired ) {
            vr.addError("", refname + " is required ");
            return;
        }
        if(element==null) return;
        if(data==null) return;
        
        //start scanner
        if(cf.getType()!=null && cf.getType().equals("list")) {
            if((data instanceof List) || data.getClass().isArray()) {
                List list = null;
                if( data instanceof List) {
                    list = (List)data;
                } else {
                    list = Arrays.asList((Object[])data);
                }
                if( cf.getMin()>0 && list.size() == 0 ) {
                    vr.addError("", refname + " must have at least " + cf.getMin() + " items");
                    return;
                }
                //validate each element.
                int i = 0;
                for(Object o : list ) {
                    ValidationResult _vr = schema.getSchemaManager().validate(element, o);
                    if( _vr.hasErrors()) {
                        _vr.setContextName( refname+"["+i+"]" );
                        vr.addSubValidation( _vr );
                    }
                    i++;
                }
                
            } else {
                vr.addError("", refname + " list or array is expected for type list ");
                return;
            }
        } else {
            //validate element. this is sub element. chain this result to
            //the main result.
            ValidationResult _vr = schema.getSchemaManager().validate(element, data);
            if( _vr.hasErrors()) {
                _vr.setContextName( refname );
                vr.addSubValidation( _vr );
            }
        }
    }
    
    
    public void endComplexField(ComplexField cf) {;}
    
    public void startLinkField(LinkField f, String refname, SchemaElement element) {;}
    public void endLinkField(LinkField f) {;}
    public void endSchema(Schema schema) {;}
    
    public ValidationResult getResult() {
        return vr;
    }
    
    
    
    
    
}
