/*
 * AbstractPageContentProvider.java
 *
 * Created on July 17, 2012, 9:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import groovy.text.Template;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Elmo
 * to be overridden by Block, Master, Widget and
 */
public abstract class PageContentCache {
    
    private Map<String, ContentTemplate> cache = new Hashtable();
    
    protected abstract InputStream getResource( String name, PageFileInstance page );
    
    //can be overridden by some properties
    public String getContent(String name, PageFileInstance pi, PageContentMap map ){
        InputStream is = null;
        try {
            if(!cache.containsKey(name)) {
                is = getResource(name, pi);
                Template temp = TemplateParser.getInstance().parse(is);
                ContentTemplate ct = new ContentTemplate(temp);
                cache.put( name, ct );
            }
            ContentTemplate ct = cache.get(name);
            return ct.render(map).toString();
        } catch(Exception ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            try { is.close(); } catch(Exception ign){;}
        }
    }
}