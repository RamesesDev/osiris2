/*
 * AbstractContent.java
 *
 * Created on June 23, 2012, 11:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import com.rameses.io.StreamUtil;
import groovy.text.Template;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Elmo
 */
public abstract class ContentHandler {
    
    private Map cache = new Hashtable();
    protected Project project;
    
    protected abstract String getName();
    public abstract InputStream getSource(String id, Object data);
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    
    public ContentTemplate getTemplate(String id, Object data) {
        if( !cache.containsKey(id) ) {
            InputStream is = null;
            try {
                is = getSource(id, data);
                if(is!=null ) {
                    Template template = TemplateParser.getInstance().parse( is );
                    cache.put(id, new ContentTemplate(id, template));
                } else {
                    return null;
                }
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
        return (ContentTemplate)cache.get(id);
    }
    
    /***
     * find a resource and return the input stream
     */
    protected InputStream findResource( String path ) {
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
    
    
    //if one needs to get a Properties file
    public Properties getProperties(String id, Object data ) {
        if( !cache.containsKey(id) ) {
            InputStream is = null;
            try {
                is = getSource(id, data);
                if(is==null) return null;
                Properties props = new Properties();
                props.load(is);
                cache.put(id, props);
            } catch(Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {is.close(); } catch(Exception ign){;}
            }
        }
        return (Properties)cache.get(id);
    }
    
    
    //if one needs to get a JSON parsed Map
    public Map getJsonMap(String id, Object data ) {
        if( !cache.containsKey(id) ) {
            InputStream is = null;
            try {
                is = getSource(id, data);
                if(is==null) return null;
                Map map = JsonUtil.toMap( StreamUtil.toString(is) );
                cache.put(id, map);
            } catch(Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {is.close(); } catch(Exception ign){;}
            }
        }
        return (Map)cache.get(id);
    }
    
}
