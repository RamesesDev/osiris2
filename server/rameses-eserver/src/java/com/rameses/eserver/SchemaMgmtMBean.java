/*
 *
 * Created on July 24, 2010, 8:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.schema.Schema;
import com.rameses.schema.SchemaManager;

/**
 *
 * @author elmo
 */
public interface SchemaMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    Schema getSchema(String name);
    void flushAll();
    void flush(String name);
    void validate(String schemaName, Object value) throws Exception ;
    
    SchemaManager getSchemaManager();
    
}
