/*
 * DBScriptProvider.java
 *
 * Created on December 6, 2009, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.resources.db;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.naming.InitialContext;


public class DBSqlResourceProvider extends AbstractDBResourceProvider {
    
    public String getName() {
        return "sql";
    }
    
    public String getDescription() {
        return "DB Sql Cache Resource Provider [sql://]";
    }

    public boolean accept(String nameSpace) {
        return (nameSpace.equalsIgnoreCase("sql"));
    }

    public InputStream getResource(String name) {
        try {
            InitialContext ctx = new InitialContext();
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
            byte[] b = ds.getSqlResource(name);
            if( b!= null )
                return new ByteArrayInputStream(b);
            else
                return null;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public int getPriority() {
        return 10;
    }

    
}
