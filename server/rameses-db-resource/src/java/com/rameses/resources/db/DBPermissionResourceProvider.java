/*
 * DBScriptProvider.java
 *
 * Created on December 6, 2009, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.resources.db;

import com.rameses.interfaces.ResourceHandler;
import com.rameses.resources.db.AbstractDBResourceProvider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import javax.naming.InitialContext;


public class DBPermissionResourceProvider extends AbstractDBResourceProvider {
    
    
    public String getName() {
        return "permission" ;
    }
    
    public String getDescription() {
        return "DB Permission Resource Provider [permission://]";
    }
    
    
    public boolean accept(String nameSpace) {
        return (nameSpace.equalsIgnoreCase("permission"));
    }

    public int getPriority() {
        return 100;
    }
    
    public InputStream getResource(String name) throws Exception {
        try {
            InitialContext ctx = new InitialContext();
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
            byte[] b = ds.getPermissionResource(name);
            if( b!= null )
                return new ByteArrayInputStream(b);
            else
                return null;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void scanResources(String name, ResourceHandler handler) throws Exception {
        InitialContext ctx = new InitialContext();
        DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
        if(name.equals("roledomains")) {
            byte[] b = ds.getConf("roledomain");
            if(b!=null) {
                handler.handle( new ByteArrayInputStream(b), "dbscript:roledomain" );    
            }    
        }
        else {
            List<String> list = ds.getPermissions();
            //scan through each permission and load it one by one.
            for(Object o :  list) {
                String n = (String)o;
                handler.handle(getResource(n), "dbscript:" + n);
            }
        }
    }
    
    
    
    
}
