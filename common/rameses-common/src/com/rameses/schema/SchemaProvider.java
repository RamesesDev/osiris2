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
    
    private SchemaConf conf;
    
    
    public abstract Schema getSchema(String name);

    public SchemaConf getConf() {
        return conf;
    }

    public void setConf(SchemaConf conf) {
        this.conf = conf;
    }
    
    
    
}
