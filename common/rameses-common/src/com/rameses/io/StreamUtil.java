/*
 * StreamUtil.java
 *
 * Created on May 21, 2009, 9:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

import java.io.InputStream;

/**
 *
 * @author elmo
 */
public final class StreamUtil {
    
    public static String toString( String filePath ) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return toString(loader.getResourceAsStream( filePath ));
    }
    
    public static String toString( InputStream is ) {
        try {
            StringBuffer out = new StringBuffer();
            int i = 0;
            while( (i=is.read())!=-1) {
                out.append((char)i);
            }
            return  out.toString();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        finally {
            try { is.close(); } catch(Exception ign){;}
        }
    }
    
    
}
