/*
 * Schema.java
 *
 * Created on August 12, 2010, 10:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SchemaElement implements Serializable {
    
    private Schema schema;
    private String name;
    private List<SchemaField> fields = new ArrayList();
    private Map<String,SchemaField> fieldMap = new Hashtable();
    private Map properties = new HashMap();
    
    
    private List<SimpleField> simpleFields;
    private List<ComplexField> complexFields;
    
    /** Creates a new instance of Schema */
    public SchemaElement(Schema schema) {
        this.schema = schema;
    }
    
    /**
     * method accessed by the parser.
     */
    public void addSchemaField(SchemaField fld) {
        fields.add( fld );
        if( fld.getName()!=null ) {
            fieldMap.put(fld.getName(), fld);
        }  
        fld.setElement(this);
    }
    
    public Map getProperties() {
        return properties;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Schema getSchema() {
        return schema;
    }
    
    public List<SchemaField> getFields() {
        return fields;
    }
    
    
    public SchemaField getField(String name) {
        return fieldMap.get(name);
    }
    
    
}
