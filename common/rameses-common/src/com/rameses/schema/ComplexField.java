/*
 * ComplexField.java
 *
 * Created on August 12, 2010, 10:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

/**
 *
 * @author elmo
 */
public class ComplexField extends SchemaField {
    
    private String name;
    private boolean required;
    private String multiplicity = ComplexFieldMultiplicity.MANY;
    private SchemaElement schema;
    
    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    public SchemaElement getSchema() {
        return schema;
    }

    public void setSchema(SchemaElement schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
    
    
}
