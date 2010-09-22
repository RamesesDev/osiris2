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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmo
 */
public final class Indexer {
    
    private EntityManager em;
    
    /** Creates a new instance of Indexer */
    public Indexer(EntityManager em) {
        this.em = em;
    }
    
    public List<String> getIndexFor( String schemaName ) {
        SchemaManager sm = em.getSchemaManager();
        SchemaElement e = em.getSchemaManager().getElement( schemaName );
        List<SchemaElement> elements = sm.lookup(schemaName, "indexfor",e.getName());
        String prefix = schemaName;
        if(prefix.indexOf(":")>0) prefix = prefix.substring(0, prefix.indexOf(":"));
        List<String> list = new ArrayList();
        for(SchemaElement se: elements) {
            list.add( prefix + ":" + se.getName() );
        }
        return list;
    }
    
    public void fire( List<String> indexers, Object data ) {
        for(String s : indexers) {
            em.create( s, data );
        }
    }
    
    
}
