/*
 * DBScriptProvider.java
 *
 * Created on December 6, 2009, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting.db;

import com.rameses.eserver.MultiResourceHandler;
import com.rameses.eserver.ResourceProvider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.naming.InitialContext;


public class DBScriptResourceProvider extends ResourceProvider {
    
    
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
            DBScriptProviderServiceLocal ds = (DBScriptProviderServiceLocal)ctx.lookup(DBScriptProviderService.class.getSimpleName()+"/local");
            byte[] b = ds.getInfo(name,DBScriptInfo.class.getName() );
            if( b!= null )
                return new ByteArrayInputStream(b);
            else
                return null;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void scanResources(String name, MultiResourceHandler handler) throws Exception {
        InitialContext ctx = new InitialContext();
        byte[] b = null;
        if(name.equals("interceptors")) {
            DBScriptProviderServiceLocal ds = (DBScriptProviderServiceLocal)ctx.lookup(DBScriptProviderService.class.getSimpleName()+"/local");
            b = ds.getInfo(name, DBConfData.class.getName());
        }
        else if(name.equals("deployers")) {
            DBScriptProviderServiceLocal ds = (DBScriptProviderServiceLocal)ctx.lookup(DBScriptProviderService.class.getSimpleName()+"/local");
            b = ds.getInfo(name, DBConfData.class.getName());
        }
        if( b!= null )
            handler.handle( new ByteArrayInputStream(b) );
    }
    
    
    
    
}
