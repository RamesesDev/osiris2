/*
 * SchemaResourceProvider.java
 *
 * Created on August 17, 2010, 1:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema.db;

import com.rameses.resources.db.DBResourceService;
import com.rameses.resources.db.DBResourceServiceLocal;
import com.rameses.schema.SchemaResourceProvider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.naming.InitialContext;



/**
 *
 * @author elmo
 */
public class DBSchemaResourceProvider implements SchemaResourceProvider {
    
    public  InputStream getResource(String name) {
        try {
            InitialContext ctx = new InitialContext();
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
            byte[] b = ds.getResource( SchemaResource.class, name);
            if( b!= null )
                return new ByteArrayInputStream(b);
            else
                return null;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    
}
