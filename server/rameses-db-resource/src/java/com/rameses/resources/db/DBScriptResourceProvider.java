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


public class DBScriptResourceProvider extends AbstractDBResourceProvider {
    
    
    public String getName() {
        return "script" ;
    }
    
    public String getDescription() {
        return "DB Script Resource Provider [script://]";
    }
    
    
    public boolean accept(String nameSpace) {
        return (nameSpace.equalsIgnoreCase("script"));
    }

    public int getPriority() {
        return 100;
    }
    
    public InputStream getResource(String name) throws Exception {
        try {
            InitialContext ctx = new InitialContext();
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
            byte[] b = ds.getScriptResource(name);
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
        byte[] b = null;
        if(name.equals("interceptors")) {
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
            //b = ds.getInfo(name, DBConfData.class.getName());
            List list = ds.getInterceptors();
            StringBuffer data = new StringBuffer();
            for(Object o: list ) {
                data.append( o + "\n");
            }
            b = data.toString().getBytes();
        }
        if( b!= null )
            handler.handle( new ByteArrayInputStream(b), "dbscript:"+name );
    }
    
    
    
    
}
