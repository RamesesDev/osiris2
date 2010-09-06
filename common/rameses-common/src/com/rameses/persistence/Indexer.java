/*
 * Indexer.java
 *
 * Created on September 4, 2010, 8:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import java.util.List;

/**
 *
 * @author elmo
 */
public final class Indexer {
    
    private DefaultEntityManager em;
    
    /** Creates a new instance of Indexer */
    public Indexer(DefaultEntityManager em) {
        this.em = em;
    }
    
    /** 
     * pass the schema and the element and the data for indexing.
     * sample : fire( "kyc:customer", data) will lookup all indexes for customer.
     */
    
    public void fire(String schemaName, Object data) {
        SchemaElement e = em.getSchemaManager().getElement( schemaName );
        fire( schemaName, "indexfor", e.getName(), data ); 
    }

    public void fire(String schemaName, String attr, String attrValue, Object data) {
        SchemaManager sm = em.getSchemaManager();
        List<SchemaElement> elements = sm.lookup(schemaName, attr,attrValue);
        String prefix = schemaName;
        if(prefix.indexOf(":")>0) prefix = prefix.substring(0, prefix.indexOf(":"));
        for(SchemaElement se: elements) {
            em.create( prefix + ":" + se.getName(), data );
        }
    }
    
}
