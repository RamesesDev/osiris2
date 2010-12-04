/*
 * CacheUtil.java
 *
 * Created on December 3, 2010, 3:33 PM
 * @author jaycverg
 */

package com.rameses.rcp.control.webbrowser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;


public class CacheUtil {
    
    private static final int BUFF_SIZE = 1024*32;
    
    
    public static URL getCache(String u, String prefix) throws Exception {
        return getCache(new URL(u), prefix);
    }
    
    public static URL getCache(URL u, String prefix) throws Exception {
        if ( prefix.contains("-") ) prefix = prefix.replace("-", "_");
        
        File dir = new File(System.getProperty("user.dir") + File.separator + "web-cache" + File.separator);
        if ( !dir.exists() ) {
            dir.mkdirs();
        }
        
        long lastModified = u.openConnection().getLastModified();
        String hash = prefix + u.toURI().toString().hashCode() + "-" + lastModified;
        File f = new File(dir, hash);
        if ( !f.exists() ) {
            cache(f, u);
        } else {
            long modified = 0;
            try {
                modified = Long.parseLong(f.getName().split("-")[1]);
            } catch(Exception e) {;}
            
            if ( lastModified != modified ) {
                cache(f, u);
            }
        }
        
        return f.toURL();
    }
    
    public static void cache(File target, URL source) throws Exception {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(target), BUFF_SIZE);
            bis = new BufferedInputStream(source.openStream(), BUFF_SIZE);
            
            byte []buffer = new byte[BUFF_SIZE];
            int bytesRead = -1;
            while( (bytesRead = bis.read(buffer)) != -1 ) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } catch(Exception e) {
            throw e;
        } finally {
            try { bos.close(); } catch(Exception e){;}
            try { bis.close(); } catch(Exception e){;}
        }
    }
    
}
