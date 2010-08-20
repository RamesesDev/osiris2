/*
 * Schema.java
 *
 * Created on August 12, 2010, 4:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class Schema implements Serializable {
    
    //usually this is the first element or the element with the same schema name.
    //or it is the first and the name is null.
    private SchemaElement rootElement;
    private String name;
    
    private Map<String,SchemaElement> elements = new Hashtable();
    
    /** Creates a new instance of Schema */
    public Schema(String n) {
        this.name = n;
        //remove if any extensions
        if( this.name.indexOf(".")>0 ) {
            this.name = this.name.substring(0, this.name.lastIndexOf("."));
        }
    }
    
    
    //we must ensure that root element must have a name.
    public SchemaElement getRootElement() {
        return rootElement;
    }

    public SchemaElement getElement(String n) {
        //special code. extracts element name from schema:element.
        String elementName = n;
        if(elementName.indexOf(":")>0) {
            elementName = elementName.substring( elementName.indexOf(":")+1 );
        }
        
        if( elementName.equals(name))
            return  rootElement;
        return elements.get(elementName);
    }
    
    /**
     * by default the root element is the first one added.
     */
    public void addElement(SchemaElement se) {
        if(rootElement==null) {
            rootElement = se;
            if(se.getName()==null ) rootElement.setName(name);
        }
        if(se.getName()!=null) {
            elements.put(se.getName(),se);
        }
    }

    public String getName() {
        return name;
    }
    
    
}
