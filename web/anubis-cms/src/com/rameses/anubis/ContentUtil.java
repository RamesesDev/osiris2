/*
 * ContentUtil.java
 *
 * Created on July 4, 2012, 10:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import com.rameses.io.StreamUtil;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Elmo
 */
public class ContentUtil {
    
    public static String correctUrlPath(String head, String context, String tail) {
        StringBuilder sb = new StringBuilder();
        sb.append( head );
        if(!head.endsWith("/")) sb.append("/");
        if(context!=null) {
            if(context.startsWith("/")) context = context.substring(1);
            sb.append(context);
            if(!context.endsWith("/")) sb.append("/");
        }
        if(tail!=null) {
            if(tail.startsWith("/")) tail = tail.substring(1);
            sb.append(tail);
        }
        return sb.toString();
    }
    
    public static boolean fileExists( String path1, String path2, String path3 ) {
        InputStream is = null;
        try {
            is = findResource(path1, path2,path3);
            return (is!=null);
        }
        catch(Exception e) {
            //do nothing
            return false;
        }
        finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
    
    public static InputStream findResource( String path1, String path2, String path3 ) {
        try {
            String path = correctUrlPath(path1,path2,path3);
            InputStream is = null;
            is  = new URL(path).openStream();
            return is;
        } catch(FileNotFoundException fe) {
            return null;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String[] tokenizePage(String name) {
        if(name.indexOf("/", 1 ) > 0 ) {
            String[] str = new String[2];
            str[0] = name.substring(1, name.indexOf("/",1));
            str[1] = name.substring( name.indexOf("/",1) );
            return str;
        }
        return null;
    }
    
    
    /**
     * locates a file, opens the inputstream then run the Json utility. 
     * if file does not exist, null is returned
     */
    public static Map getJsonMap(URL u) {
        InputStream is = null;
        try {
            is = u.openStream();
            if(is==null) return null;
            return JsonUtil.toMap( StreamUtil.toString(is) );
        } catch(FileNotFoundException fe) {
            return null;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {is.close(); } catch(Exception ign){;}
        }
    }
    
    public static Map getJsonMap(InputStream is) {
        try {
            if(is==null) return null;
            return JsonUtil.toMap( StreamUtil.toString(is) );
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {is.close(); } catch(Exception ign){;}
        }
    }
    
    public static Map getJsonMap(String path1, String path2, String path3) {
        InputStream is = null;
        try {
            is = findResource(path1, path2, path3);
            if(is==null) return null;
            return JsonUtil.toMap( StreamUtil.toString(is) );
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {is.close(); } catch(Exception ign){;}
        }
    }
    
    public static Map getProperties(String path1, String path2, String path3) {
        InputStream is = null;
        try {
            is = findResource(path1, path2,path3);
            if(is==null) return null;
            Properties props = new Properties();
            props.load(is);
            return props;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {is.close(); } catch(Exception ign){;}
        }
    }
    
    public static Map getProperties(URL u) {
        InputStream is = null;
        try {
            is = u.openStream();
            if(is==null) return null;
            Properties props = new Properties();
            props.load(is);
            return props;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {is.close(); } catch(Exception ign){;}
        }
    }
   
}
