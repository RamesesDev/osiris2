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


public class Schema implements Serializable {
    
    /**
     * this is the manager that created this schema.
     */
    private SchemaManager schemaManager;
    
    //usually this is the first element or the element with the same schema name.
    //or it is the first and the name is null.
    private SchemaElement rootElement;
    private String name;
    
    private Map<String,SchemaElement> elements = new Hashtable();

    private Map<String,SchemaField> schemaFields = new Hashtable();
    
    
    /** Creates a new instance of Schema */
    Schema(String n, SchemaManager sm) {
        this.name = n;
        //remove if any extensions
        if( this.name.indexOf(".")>0 ) {
            this.name = this.name.substring(0, this.name.lastIndexOf("."));
        }
        this.schemaManager = sm;
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
        SchemaElement e = elements.get(elementName);
        if(e==null)
            throw new RuntimeException("Schema element " + n + " not found in "+name);
        return e;
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
    
    /**
     * we follow path : element/fieldname
     * example: order/customer
     */
    private SchemaField _findField( SchemaElement element, String fieldName ) {
        SchemaField retVal = null;
        for(SchemaField sf : element.getFields() ) {
            if(sf instanceof SimpleField) {
                if(sf.getName().equals(fieldName)) {
                    retVal = sf;
                    break;
                }
            }
            else if(sf instanceof LinkField) {
                LinkField lf = (LinkField)sf;
                if(fieldName.equals(lf.getName()) || fieldName.equals(lf.getRef())) {
                    return lf;
                }
                else if( (!lf.isPrefixed()) ||
                        (lf.isPrefixed() && fieldName.startsWith(lf.getName()))) {
                    String ref = lf.getRef();
                    if(ref==null)
                        throw new RuntimeException("Linkfield ref must not be null for element " +element.getName() + "/" + fieldName);
                    
                    SchemaElement se = getElement(ref);
                    if(se==null)
                        throw new RuntimeException("Linked ref element " +ref + " not found");
                    
                    //remove the prefix from the field name.
                    if(lf.isPrefixed() )
                        fieldName = fieldName.substring( lf.getName().length()+1 );
                    retVal = _findField( se, fieldName );
                    if(retVal!=null) break;
                }
            }
        }    
        return retVal;
    }
    
    
    public SchemaField findField( String path ) {
        if(schemaFields.containsKey(path)) {
            return schemaFields.get(path);
        }

        String elementName = path.substring(0, path.indexOf("/"));
        String fieldName = path.substring(path.indexOf("/")+1 );
        SchemaElement element = getElement(elementName);
        SchemaField sf = _findField( element, fieldName );
        if(sf==null) throw new RuntimeException("schema field " + path + " does not exist!");
        schemaFields.put(path, sf);
        return sf;
    }

    public SchemaManager getSchemaManager() {
        return schemaManager;
    }
    
    
}
