/*
 * SchemaProvider.java
 *
 * Created on August 13, 2010, 8:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

public abstract class SchemaProvider {
    
    private SchemaManager schemaManager;
    
    
    public abstract Schema getSchema(String name);

    public SchemaManager getSchemaManager() {
        return schemaManager;
    }

    public void setSchemaManager(SchemaManager sm) {
        this.schemaManager = sm;
    }
    
    
    
}
