/*
 * DBScriptProvider.java
 *
 * Created on December 6, 2009, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql.db;

import com.rameses.resources.db.*;
import com.rameses.sql.SqlUnitResourceProvider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.naming.InitialContext;


public class DBSqlResourceProvider implements SqlUnitResourceProvider {

    public InputStream getResource(String name) {
        try {
            InitialContext ctx = new InitialContext();
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
            byte[] b = ds.getResource(SqlResource.class, name);
            if( b!= null )
                return new ByteArrayInputStream(b);
            else
                return null;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    
}
