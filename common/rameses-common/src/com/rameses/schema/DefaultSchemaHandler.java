/*
 * DefaultSchemaHandler.java
 *
 * Created on August 16, 2010, 7:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

/**
 *
 * this class is just a templae and does nothing significant.
 */
public class DefaultSchemaHandler implements SchemaHandler {
    
    protected SchemaHandlerStatus status;
    
    /** Creates a new instance of DefaultSchemaHandler */
    public DefaultSchemaHandler() {
    }

    public void startSchema(Schema schema) {
    }

    public void startElement(SchemaElement element, Object data) {
    }

    public void processField(SimpleField f, String refname, Object value) {
    }

    public void startLinkField(LinkField f, String refname, SchemaElement element) {;}

    public void endLinkField(LinkField f) {;}


    public void endComplexField(ComplexField cf) {
    }

    public void endElement(SchemaElement element) {
    }

    public void endSchema(Schema schema) {
    }

    public void setStatus(SchemaHandlerStatus status) {
        this.status = status;
    }

    public void startComplexField(ComplexField cf,String refname, SchemaElement element,Object data) {
    }
    
}
