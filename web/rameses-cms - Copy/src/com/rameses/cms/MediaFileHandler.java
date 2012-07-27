/*
 * MediaFileHandler.java
 *
 * Created on June 19, 2012, 11:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.io.InputStream;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class MediaFileHandler extends FileHandler  {
    
    public String getExt() {
        return "media";
    }

    public AbstractFile createFile(String id, Map props) {
        return new Media(props);
    }

    public InputStream getContent(Object o) {
        return  null;
    }
    
}
