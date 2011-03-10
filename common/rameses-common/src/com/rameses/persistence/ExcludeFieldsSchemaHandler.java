/*
 * DefaultSchemaHandler.java
 *
 * Created on August 16, 2010, 7:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import com.rameses.schema.*;
import java.util.Map;

/**
 *
 * this class is just a templae and does nothing significant.
 */
public class ExcludeFieldsSchemaHandler implements SchemaHandler {
    
    private Map map;

    protected SchemaHandlerStatus status;
    
    /** Creates a new instance of DefaultSchemaHandler */
    public ExcludeFieldsSchemaHandler(Map data) {
        this.map = data;
    }

    public void startSchema(Schema schema) {
    }

    public void startElement(SchemaElement element, Object data) {
        
    }
    
    public void processField(SimpleField f, String refname, Object value) {
        map.remove( f.getName() );
    }
    
    public void startComplexField(ComplexField cf,String refname, SchemaElement element,Object data) {
        map.remove(cf.getName());
    }

    public void startLinkField(LinkField f, String refname, SchemaElement element) {
    }

    public void endLinkField(LinkField f) {
    }


    public void endComplexField(ComplexField cf) {
    }

    public void endElement(SchemaElement element) {
    }

    public void endSchema(Schema schema) {
    }

    public void setStatus(SchemaHandlerStatus status) {
        this.status = status;
    }

   
    
}
