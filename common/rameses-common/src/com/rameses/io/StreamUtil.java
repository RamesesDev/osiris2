/*
 * StreamUtil.java
 *
 * Created on May 21, 2009, 9:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        InputStreamReader isr = null;
        BufferedReader br = null;  
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            StringBuffer out = new StringBuffer();
            String s = null;
            while( (s=br.readLine())!=null) {
                out.append(s + "\n");
            }
            return  out.toString();
        }
        catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
        finally {
            try { is.close(); } catch(Exception ign){;}
            try { isr.close(); } catch(Exception ign){;}
            try { br.close(); } catch(Exception ign){;}
        }
    }
    
}
