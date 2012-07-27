/*
 * FileUtils.java
 *
 * Created on July 19, 2012, 9:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import com.rameses.util.URLDirectory;
import com.rameses.util.URLDirectory.URLFilter;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author Elmo
 * Utility to aid in scanning files
 * and investigating the files being scanned
 */
public class FileDir {
    
    //this 
    public static class FileInfo {
        private String ext;
        private URL url;
        private String fileName;
        private String parentPath;
        private boolean dir;
        private String name;
        
        public FileInfo(URL u) {
            url = u;
            String _fileName = u.toString();
            if( _fileName.endsWith("/")) {
                _fileName = _fileName.substring(0, _fileName.length()-1);
                dir = true;
            }
            int pos = _fileName.lastIndexOf("/");
            fileName = _fileName.substring(pos+1);
            name = fileName;
            parentPath = _fileName.substring(0, pos);
            if( fileName.indexOf(".")>0) {
                ext = fileName.substring(fileName.lastIndexOf(".")+1);
                name = fileName.substring(0, fileName.lastIndexOf("."));
            }
        }

        public String getExt() {
            return ext;
        }
        public URL getUrl() {
            return url;
        }
        public String getFileName() {
            return fileName;
        }
        public String getParentPath() {
            return parentPath;
        }

        public boolean isDir() {
            return dir;
        }
        
        //checks if a file exists with the particular filename under this directory
        public boolean isFileExists( String fileName ) {
            if( !dir ) return false;
            InputStream is = null;
            try {
                if( fileName.startsWith("/")) fileName = fileName.substring(1);
                URL u = new URL(url+fileName);
                is = u.openStream();
                return ( is !=null );
            }
            catch(Exception e){
                return false;
            }
            finally {
                try { is.close();} catch(Exception ign){;}
            }
        }
        
        //returns a child file. If file does not exist, null is returned
        public URL getSubfile( String fileName ) {
            if( !dir ) return null;
            InputStream is = null;
            try {
                if( fileName.startsWith("/")) fileName = fileName.substring(1);
                URL u = new URL(url+fileName);
                is = u.openStream();
                if(is==null) return null;
                return u;    
            }
            catch(Exception e){
                return null;
            }
            finally {
                try { is.close();} catch(Exception ign){;}
            }
        }

        public String getName() {
            return name;
        }
        
    }
    
    public static abstract class FileFilter implements URLFilter {
        public abstract void handle(FileInfo f);
        public boolean accept(URL u, String filter) {
            FileInfo fileInfo = new FileInfo(u);
            handle(fileInfo);
            return false;
        }
    }
    
    
    public static void scan(String urlPath, FileFilter filter) {
        try {
            scan( new URL(urlPath), filter);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
     public static void scan(URL url, FileFilter filter) {
        try {
            URLDirectory ud = new URLDirectory(url);
            ud.list( filter );
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
}
