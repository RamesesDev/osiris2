package com.rameses.scripting;

import java.io.InputStream;
import java.net.URL;


public class DefaultScriptLoader implements ScriptLoader {
    
    private ScriptManager scriptManager;
    
    public DefaultScriptLoader(ScriptManager sm) {
        this.scriptManager = sm;
    }

    public InputStream findResource(String name) {
        try {
            String fileName = "META-INF/scripts/" + name;
            URL u = Thread.currentThread().getContextClassLoader().getResource( fileName );
            return u.openStream();
        } 
        catch(Exception ign) {
            return null;
        }
    }
    
    public ScriptObject findScript(String name) {
        //find script first in the database. If it cannot be found, find in META-INF/scripts
        InputStream is = null;
        try {
            if(is==null) is = findResource(name);
            if( is == null )
                throw new Exception("Script " + name + " not found");
            Class clazz = scriptManager.getScriptProvider().parseClass( is );
            
            String proxyInterface = InterfaceBuilder.getProxyInterfaceScript(name, clazz);
            Class proxyClass = null;
            if(proxyInterface!=null) {
                proxyClass = scriptManager.getScriptProvider().parseClass( proxyInterface );
            }
            //build also the proxy class so it can be done on one pass only.
            return new ScriptObject(clazz, name, proxyInterface, proxyClass );
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try { is.close(); } catch(Exception ign){;}
        }
    }

    
}
