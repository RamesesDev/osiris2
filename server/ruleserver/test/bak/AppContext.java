/*
 * LocalScriptService.java
 *
 * Created on October 13, 2010, 12:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bak;


import com.rameses.util.URLUtil;
import java.net.URL;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public final class AppContext {
    
    private static String name;
    
    public static String getName() {
        if(name==null) {
            try {
                URL u1 = Thread.currentThread().getContextClassLoader().getResource( "META-INF/application.xml" );
                if(u1!=null) {
                    URL u2 = URLUtil.getParentUrl( u1 );
                    URL u3 = URLUtil.getParentUrl( u2 );
                    String p = u3.getPath();
                    name = p.substring(p.lastIndexOf("/")+1, p.lastIndexOf(".") );
                } else {
                    name = "";
                }
            } catch(Exception ign) {
                name = "";
            }
        }
        return name;
    }
    
    public static String getPath() {
        String sname = getName();
        if(sname!=null && sname.trim().length()>0) return sname + "/";
        else return name;
    }
    
    public static final DataSource getSystemDs() {
        try {
            InitialContext ctx = new InitialContext();
            return (DataSource)ctx.lookup("java:" + getName() + "_system");
        }
        catch(Exception e) {
            throw new RuntimeException("AppSystemUtil.Error lookup systemDB. " + e.getMessage() );
        }
    }
    
    public static final boolean hasAppName() {
        String name = getName();
        if(name==null || name.trim().length()==0) return false;
        return true;
    } 
    
    public static final DataSource lookupDs(String name) {
        try {
            if(name.startsWith("java:")) name = name.substring(5);
            if(hasAppName() && !name.startsWith(getName())) name = getName() + "_" + name; 
            
            InitialContext ctx = new InitialContext();
            return (DataSource)ctx.lookup("java:" + name);
        }
        catch(Exception e) {
            throw new RuntimeException("AppContext.lookupDs error. " + e.getMessage() );
        }
    }
    
    
}
