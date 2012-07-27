/*
 * ContentUtil.java
 *
 * Created on June 12, 2012, 11:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import com.rameses.util.ObjectDeserializer;
import com.rameses.util.TemplateSource;
import groovy.text.Template;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Elmo
 */
public class ContentUtil {
    
    
    
    public static ContentTemplate findTemplate( String name, Map store, TemplateSource templateSource ) throws Exception {
        if( !store.containsKey(name) ) {
            InputStream is = templateSource.getSource(name);
            if(is!=null ) {
                Template template = TemplateParser.getInstance().parse( is );
                store.put(name, new ContentTemplate(name, template));
            } else {
                return null;
            }
        }
        return (ContentTemplate) store.get(name);
    }
    
    
    
    /***
     * find a resource and return the input stream
     */
    public static InputStream findResource( String path ) {
        try {
            InputStream is = null;
            is  = new URL(path).openStream();
            return is;
        } catch(FileNotFoundException fe) {
            return null;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /***
     * find a file in a properties format, convert to inputstream
     * finally to a Properties object.
     */
    public static Map findResourceToProperties(String path) {
        InputStream is = null;
        ObjectInputStream ois = null;
        try {
            URL u = new URL(path);
            is = u.openStream();
            Properties props = new Properties();
            props.load(is);
            return props;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {ois.close(); } catch(Exception ign){;}
            try {is.close(); } catch(Exception ign){;}
        }
    }
    
    public static Properties URLtoProperties(URL u ) {
        InputStream is = null;
        try {
            is = u.openStream();
            if(is==null) return null;
            Properties props = new Properties();
            props.load(is);
            return props;
        } catch(Exception e) {
            return null;
        } finally {
            try {is.close(); } catch(Exception ign){;}
        }
    }
    
}
