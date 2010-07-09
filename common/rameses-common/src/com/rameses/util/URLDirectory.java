/*
 * URLDirectory.java
 *
 * Created on February 23, 2009, 11:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author elmo
 */
public class URLDirectory {
    
    private URL url;
    
    public URLDirectory(URL u) {
        this.url = u;
    }
    
    public URL[] list(ClassLoader loader) {
        return list(null, loader);
    }
    
    public URL[] list(URLFilter filter, ClassLoader loader) {
        try {
            List list = new ArrayList();
            String s = url.toURI() + "";
            URLConnection conn = url.openConnection();
            if( conn instanceof JarURLConnection  ) {
                JarURLConnection jurl = (JarURLConnection)conn;
                JarFile jf = jurl.getJarFile();
                String dirName = s.substring(s.indexOf("jar!")+5);
                Enumeration ee = jf.entries();
                while(ee.hasMoreElements()) {
                    JarEntry je = (JarEntry)ee.nextElement();
                    if( je.getName().startsWith(dirName) && !je.getName().equals(dirName) ) {
                        String spath = url.toURI().toString().replaceAll(dirName,"") + je.getName();
                        URL u = new URL( spath );
                        if( filter == null || filter.accept(u, u.getPath())) {
                            list.add( u );
                        }
                    }
                }
            } 
            else  {
                File f = new File(url.toURI());
                if( f.isDirectory() ) {
                    File[] files = f.listFiles();
                    for( int i=0; i<files.length; i++ ) {
                        URL u = files[i].toURL();
                        if( filter == null || filter.accept(u, u.getPath()) ) {
                            list.add( u );
                        }
                    }
                }
            }
            return (URL[])list.toArray(new URL[]{});
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    
    public static interface URLFilter {
        boolean accept(URL u, String filter);
    }

    
}
