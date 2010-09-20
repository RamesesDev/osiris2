/*
 * URLUtil.java
 *
 * Created on September 12, 2010, 7:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.net.URL;

/**
 *
 * @author elmo
 */
public final class URLUtil {
    
    //this returns the parent URL. Just removes the last /
    public static URL getParentUrl( URL u ) {
        try {
            String s = u.toExternalForm();
            s = s.substring( 0, s.lastIndexOf("/") );
            return  new URL( s );
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static URL getSubUrl( URL u, String subDirectory ) {
        try {
            String s = u.toExternalForm();
            if(subDirectory.startsWith("/")) subDirectory = subDirectory.substring(1);
            if( s.endsWith("/")) {
                s = s + subDirectory;
            } else {
                s = s + "/" + subDirectory;
            }
            return  new URL( s );
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
