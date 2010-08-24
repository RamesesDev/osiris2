/*
 * SchemaField.java
 *
 * Created on August 12, 2010, 10:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

/**
 *
 * @author elmo
 */
public class SimpleField extends SchemaField implements SimpleFieldTypes {
    
    private String name;
    private boolean required;
    private String type;
    
    
    /**
     * if this is provided, this will override the bean mapping.
     */
    private String mapfield;

    public SimpleField() {
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String dataType) {
        this.type = dataType;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    
    private String _description;
    public String toString() {
        if(_description==null) {
            _description = (name==null ? "_unnamed" : name)  
            + (type!=null ? "[" + type + "]":" ")
            + ( required ? " required " : "" );
        }
        return _description;
    }

    public String getMapfield() {
        return mapfield;
    }

    public void setMapfield(String mapfield) {
        this.mapfield = mapfield;
    }

    
    
    
}
