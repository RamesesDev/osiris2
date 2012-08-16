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
import java.net.HttpURLConnection;
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
    
    public URL[] list(URLFilter filter) {
        return list(filter, null);
    }
    
    //class loader should eventually be deprecated
    public URL[] list(URLFilter filter, ClassLoader loader) {
        try {
            List list = new ArrayList();
            String s = url.toString() + "";
            URLConnection conn = url.openConnection();
            if( conn instanceof JarURLConnection  ) {
                JarURLConnection jurl = (JarURLConnection)conn;
                JarFile jf = jurl.getJarFile();
                String dirName = s.substring(s.indexOf("!")+1);
                String pattern = dirName +"/[^/]*(/|.)$";
                String contextPart = s.substring(0, s.indexOf("!"));
                Enumeration ee = jf.entries();
                while(ee.hasMoreElements()) {
                    JarEntry je = (JarEntry)ee.nextElement();
                    if( !("/"+je.getName() ).matches(pattern)) continue;
                    //process only the immediate children
                    String spath = contextPart + "!/"+ je.getName();
                    //System.out.println(spath);
                    URL u = new URL( spath );
                    //System.out.println("---->"+u.toString());
                    if( filter == null || filter.accept(u, u.getPath())) {
                        list.add( u );
                    }
                }
            } else if( conn instanceof HttpURLConnection ) {
                throw new Exception("Http URL Connection is not supported at this time.");
            } else {
                File f = new File(url.getFile());
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
            System.out.println("URL DIRECTORY ERROR " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
    
    
    public static interface URLFilter {
        boolean accept(URL u, String filter);
    }
    
    public static void scanFiles( URL u, int maxLevels, URLScanHandler handler ) {
        handler.start();
        _scanFileDir( u, 0, maxLevels, handler );
        handler.end();
    }
    
    private static void _scanFileDir( URL url,  int i, int maxLevels, URLScanHandler handler ) {
        if( i > maxLevels ) return;
        handler.handle( url, i );
        i++;
        handler.startChildren(i);
        URLDirectory dir = new URLDirectory(url);
        for(URL u : dir.list(null) ) {
            _scanFileDir(u, i, maxLevels, handler );
        }
        handler.endChildren(i);
    }
    
    public static interface URLScanHandler {
        void start();
        void startChildren(int level);
        void handle(URL u, int level);
        void endChildren( int level);
        void end();
    }
}
