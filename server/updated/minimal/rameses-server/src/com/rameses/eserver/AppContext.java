/*
 * LocalScriptService.java
 *
 * Created on October 13, 2010, 12:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;


import com.rameses.util.ExprUtil;
import com.rameses.util.SysMap;
import com.rameses.util.URLUtil;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author ms
 */
public final class AppContext {
    
    private static Properties properties;
    private static Map sysmap;
    private static String name;
    private static String path;
    private static String host;
    private static DataSource systemDs;
    private static String appPathDir;
    
    
    //this should load all app.conf defined.
    public static void load() {
        loadName();
        loadHost();
        loadProperties();
        loadSystemDs();
    }
    
    private static void loadName() {
        try {
            URL u1 = Thread.currentThread().getContextClassLoader().getResource( "META-INF/application.xml" );
            if(u1!=null) {
                URL u2 = URLUtil.getParentUrl( u1 );    
                URL u3 = URLUtil.getParentUrl( u2 );    
                appPathDir = u3.getPath();
                if(appPathDir.startsWith("/")) appPathDir = appPathDir.substring(1);
                name = appPathDir.substring(appPathDir.lastIndexOf("/")+1, appPathDir.lastIndexOf(".") );
            } else {
                name = "";
            }
        } catch(Exception ign) {
            name = "";
        }
        
        //loads the path
        if(name!=null && name.trim().length()>0) 
            path = name + "/";
        else 
            path = name;
    }

    private static void loadHost() {
        host = System.getProperty("jboss.bind.address");
        if(host==null) host = System.getProperty( name + ".host" );
    }

    private static void loadProperties() {
        //load properties
        properties = new Properties();
        InputStream is = null;
        try {
            Properties props = new Properties();
            Enumeration<URL> e =  Thread.currentThread().getContextClassLoader().getResources("META-INF/app.conf");
            while(e.hasMoreElements()) {
                props.clear();
                URL u = e.nextElement();
                is = u.openStream();
                props.load(is);
                properties.putAll(props);
            }
            
            //built-in reserved keywords
            properties.put("app.name", getName());
            properties.put("app.path.dir", getAppPathDir());
            
            sysmap = new SysMap( properties );
        } 
        catch(Exception ex) {
            System.out.println("error loading app properties " + ex.getMessage());
        } 
        finally {
            try {is.close();}catch(Exception ign){;}
        }
    }

    private static void loadSystemDs() {
        try {
            InitialContext ctx = new InitialContext();
            systemDs = (DataSource)ctx.lookup("java:" + getName() + "_system");
        } catch(Exception e) {
            throw new RuntimeException("AppSystemUtil.Error lookup systemDB. " + e.getMessage() );
        }        
    }
    
    public static String getName() {
        return name;
    }
    
    public static String getPath() {
        return path;
    }
    
    public static final DataSource getSystemDs() {
        return systemDs;
    }
    
    public static final boolean hasAppName() {
        if(name==null || name.trim().length()==0) return false;
        return true;
    }
    
    public static final DataSource lookupDs(String dname) {
        try {
            if(dname.startsWith("java:")) dname = dname.substring(5);
            if(hasAppName() && !dname.startsWith(getName())) dname = getName() + "_" + dname;
            
            InitialContext ctx = new InitialContext();
            return (DataSource)ctx.lookup("java:" + dname);
        } catch(Exception e) {
            throw new RuntimeException("AppContext.lookupDs error. " + e.getMessage() );
        }
    }
 
    
    
    public static Map getProperties() {
        return properties;
    }
    
    public static Map getSysMap() {
        return sysmap;
    }
    
    public static Object getProperty( String pname ) {
        Object result =  sysmap.get( pname );
        if(result!=null && (result instanceof String)) {
            result = ExprUtil.substituteValues( (String)result, sysmap );
        }
        return result;
    }
    
    public static String getHost() {
        return host;
    }

    public static String getAppPathDir() {
        return appPathDir;
    }
    
}
