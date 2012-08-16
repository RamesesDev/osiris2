/*
 * CustomMediaFileHandler.java
 *
 * Created on July 4, 2012, 8:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.File;
import com.rameses.anubis.FileInstance;
import com.rameses.anubis.MediaFileHandler;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author Elmo
 */
public class CustomMediaFileHandler extends MediaFileHandler {
    
    private static String MEDIA_DIR = "content/media";
    
    public FileInstance createInstance(File file) {
        return new CustomMediaFileInstance(file);
    }
    
    public static class CustomMediaFileInstance extends FileInstance {
        public CustomMediaFileInstance(File f) {
            super(f);
        }
        public InputStream getContent() {
            try {
                String filename = (String) getId();
                filename = filename.substring(0, filename.lastIndexOf("."));
                String path = ContentUtil.correctUrlPath( super.getProject().getUrl(), MEDIA_DIR ,filename);
                return new URL(path).openStream();
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
}
