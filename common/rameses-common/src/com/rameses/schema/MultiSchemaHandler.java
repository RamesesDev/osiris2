/*
 * MultiSchemaHandler.java
 *
 * Created on August 16, 2010, 11:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.util.ArrayList;
import java.util.List;

/***
 * special schema handler for handling multiple schema handlers
 */
public class MultiSchemaHandler implements SchemaHandler {
    
    private List<SchemaHandler> handlers = new ArrayList();
    
    public void addHandler(SchemaHandler handler) {
        handlers.add( handler );
    }
    
    public void removeHandler(SchemaHandler handler) {
        handlers.remove( handler );
    }
    
    public void clearHandlers() {
        handlers.clear();
    }
    
    public void setStatus(SchemaHandlerStatus status) {
        for(SchemaHandler h: handlers) h.setStatus(status);
    }

    public void startSchema(Schema schema) {
        for(SchemaHandler h: handlers) h.startSchema(schema);
    }

    public void startElement(SchemaElement element, Object data) {
        for(SchemaHandler h: handlers) h.startElement(element,data);
    }

    public void processField(SimpleField f, String refname, Object value) {
        for(SchemaHandler h: handlers) h.processField(f, refname, value);        
    }

    public void startLinkField(LinkField f) {
        for(SchemaHandler h: handlers) h.startLinkField(f);        
    }

    public void startComplexField(ComplexField cf) {
        for(SchemaHandler h: handlers) h.startComplexField(cf);
    }

    public void endLinkField(LinkField f) {
        for(SchemaHandler h: handlers) h.endLinkField(f);        
    }

    public void endComplexField(ComplexField cf) {
        for(SchemaHandler h: handlers) h.endComplexField(cf);        
    }

    public void endElement(SchemaElement element) {
        for(SchemaHandler h: handlers) h.endElement(element);        
    }

    public void endSchema(Schema schema) {
        for(SchemaHandler h: handlers) h.endSchema(schema);        
    }
    
}
