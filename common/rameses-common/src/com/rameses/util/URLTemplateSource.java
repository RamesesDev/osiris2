/*
 * UrlTemplateSource.java
 *
 * Created on May 7, 2012, 8:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author Elmo
 */
public class URLTemplateSource implements TemplateSource {
    
    private String rootUrl;
   
    public URLTemplateSource(String u) {
        this.rootUrl = u;
        if( !u.endsWith("/")) this.rootUrl = this.rootUrl+"/"; 
    }

    public InputStream getSource(String name) {
        try {
            if( name.startsWith("/")) name = name.substring(1);
            return (new URL(rootUrl+name)).openStream();
        }
        catch(FileNotFoundException fnf) {
            return null;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    
}
