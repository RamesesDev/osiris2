/*
 * StreamUtil.java
 *
 * Created on May 21, 2009, 9:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author elmo
 */
public final class StreamUtil {
    
    public static String toString( String filePath ) {
        ClassLoader loader = StreamUtil.class.getClassLoader();
        return toString(loader.getResourceAsStream( filePath ));
    }
    
    public static String toString( InputStream is ) {
        try {
            StringBuffer out = new StringBuffer();
            int i = -1;
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
    
    public static void write(InputStream src, OutputStream dest) {
        try {
            int b = -1;
            while( (b=src.read())!=-1 ) {
                dest.write(b);
            }
            dest.flush();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        finally {
            try { src.close(); }catch(Exception ign){;}
            try { dest.close(); }catch(Exception ign){;}
        }
    }
    
    public static void write(InputStream src, OutputStream dest, int bufferSize) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(src, bufferSize);
            bos = new BufferedOutputStream(dest, bufferSize);
            byte []buffer = new byte[bufferSize];
            int read = -1;
            while( (read=bis.read(buffer))!=-1 ) {
                bos.write(buffer, 0, read);
            }
            bos.flush();
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        finally {
            try { bis.close(); }  catch(Exception ign){;}
            try { bos.close(); }  catch(Exception ign){;}
            try { src.close(); }  catch(Exception ign){;}
            try { dest.close(); } catch(Exception ign){;}
        }
    }

}
