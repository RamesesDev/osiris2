/*
 * FileUtil.java
 *
 * Created on June 2, 2010, 4:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author elmo
 */
public final class FileUtil {
    
    public static Object readObject(File f ) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try { fis.close(); } catch (Exception e) {;}
            try { ois.close(); } catch (Exception e) {;}
        }
    }
    
    public static void writeObject(File f, Object o ) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            if(f.exists()) f.delete();
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject( o );
            oos.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try { fos.close(); } catch (Exception e) {;}
            try { oos.close(); } catch (Exception e) {;}
        }
    }
    
    //fast way of getting
    public static void extractStringFromFile( String pattern, StringBuffer sout, FileInputStream input ) throws Exception {
        FileChannel channel = input.getChannel();
        int fileLength = (int)channel.size();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY,0,fileLength);
        
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = decoder.decode(buffer);
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(charBuffer);
        int start = 0;
        int end = 0;
        boolean started = false;
        while(matcher.find()) {
            if( !started ) {
                started = true;
                start = matcher.end();
            } else {
                end = matcher.start();
                break;
            }
        }
        sout.append( charBuffer.subSequence(start, end) );
    }
    
    public static void copy(File source, File dest) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            if ( dest.isDirectory() ) {
                dest = new File(dest, source.getName());
            }
            bos = new BufferedOutputStream(new FileOutputStream( dest ));
            bis = new BufferedInputStream(new FileInputStream( source ));
            byte[] buff = new byte[10240];
            int bytesRead = -1;
            while( (bytesRead = bis.read(buff)) != -1 ) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
        }
        catch(Exception e) {
            
        }
        finally {
            try { bos.close(); } catch(Exception e){;}
            try { bis.close(); } catch(Exception e){;}
        }
    }
    
    public static boolean deleteRecursive( File path ) {
        if( !path.exists() ) return false;
        if( path.isDirectory() ) {
            File[] files = path.listFiles();
            for( int i=0; i<files.length; i++) {
                deleteRecursive( files[i] );
            }
        }
        return path.delete();
    }
    
    
}
