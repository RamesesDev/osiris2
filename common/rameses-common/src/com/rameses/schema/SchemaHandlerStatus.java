/*
 * SchemaScanStatus.java
 *
 * Created on August 16, 2010, 10:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.util.Iterator;
import java.util.Stack;

/**
 * this class holds the status of the fields during parsing.
 */
public class SchemaHandlerStatus {
    
    private Schema schema;
    private Stack<LinkField> linkFieldStack = new Stack();
    
    SchemaHandlerStatus(Schema schema) {
        this.schema = schema;
    }

    /**
     * checks parent linkfield list if we should exclude this field or not.
     */
    public boolean isExcludeField( SimpleField sf ) {
        boolean exclude = false;
        if(!linkFieldStack.isEmpty()) {
            LinkField lf =linkFieldStack.peek();
            String excludeNames = lf.getExclude();
            if(excludeNames!=null && sf.getName().matches(excludeNames)) exclude = true;
        }
        return exclude;
    }
    
    /**
     * returns the corrected field name if has prefix or not.
     * if null is returned, field should not be included.
     * the prefixed name should work back all link fields.
     */
    public String getFixedFieldName( SimpleField sf ) {
        String context = getContextPath();
        if(context!=null && context.trim().length()>0)
            return context + "_" + sf.getName();
        else
            return sf.getName();
    }

    public String getContextPath() {
        StringBuffer buff = null;
        if(!linkFieldStack.isEmpty()) {
            buff = new StringBuffer();
            Iterator<LinkField> iter = linkFieldStack.iterator();
            int i = 0;
            while(iter.hasNext()) {
                LinkField lf = iter.next();
                if(lf.isPrefixed()) {
                    if(i++>0) buff.append("_");
                    buff.append(  lf.getName() );
                }
            }
            return buff.toString();
        }
        return null;
    }
    
    public void pushLinkField(LinkField lf) {
        linkFieldStack.push( lf );
    }
    
    public void popLinkField() {
        linkFieldStack.pop();
    }
    
    public LinkField getFieldContext() {
        if( !linkFieldStack.empty() )
            return linkFieldStack.peek();
        else
            return null;
    }
    
    public SchemaElement getLinkedElement() {
        if( getFieldContext()==null )
            return null;
        else {
            return schema.getElement( getFieldContext().getRef() );
        }
    }
    
    
}
