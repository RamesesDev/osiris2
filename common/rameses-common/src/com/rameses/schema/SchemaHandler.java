/*
 * SchemaHandler.java
 *
 * Created on August 14, 2010, 1:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

/**
 *
 * @author elmo
 */
public interface SchemaHandler {
    
    void setStatus( SchemaHandlerStatus status );
    
    void startSchema(Schema schema);
    void startElement( SchemaElement element, Object data);
    void processField(SimpleField f, String refname, Object value);
    void startLinkField(LinkField f);
    void startComplexField(ComplexField cf);
    void endLinkField(LinkField f);
    void endComplexField(ComplexField cf);
    void endElement(SchemaElement element);
    void endSchema(Schema schema);
    
}
