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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import javax.naming.InitialContext;


public class DBConfResourceProvider extends AbstractDBResourceProvider {
    
    public String getName() {
        return "conf" ;
    }
    
    public String getDescription() {
        return "DB Conf Resource Provider [conf://]";
    }
    
    
    public boolean accept(String nameSpace) {
        return (nameSpace.equalsIgnoreCase("conf"));
    }
    
    public int getPriority() {
        return 100;
    }
    
    public void scanResources(String name, ResourceHandler handler) throws Exception {
        if(name.equals("vars")) {
            InitialContext ctx = new InitialContext();
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
            List<String> list = ds.getConfCategory( "vars" );
            InputStream is = null;
            for(String n: list) {
                byte[] b = ds.getConf(n);
                if(b!=null) {
                    try {
                        is = new ByteArrayInputStream(b);
                        handler.handle( is, "dbconf:" + n );
                    } catch(Exception ign){
                        
                    } finally {
                        try {is.close();}catch(Exception ign){;}
                    }
                }
            }
        }
    }
    
    public InputStream getResource(String name) throws Exception {
        InitialContext ctx = new InitialContext();
        DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
        byte[] b = ds.getConf(name);
        if(b!=null)
            return new ByteArrayInputStream(b);
        else
            throw new Exception("Resource " + name + " not found");
    }
    
    
}
