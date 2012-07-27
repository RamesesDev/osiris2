/*
 * Folder.java
 *
 * Created on June 19, 2012, 8:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Elmo
 */
public class Folder extends AbstractFile {
    
    private List<AbstractFile> items;
    private List<String> ids = new ArrayList();
    private List<UrlMapping> urlMappings;
    
    public Folder(Map info) {
        this.ids = (List)info.remove("items");
        this.urlMappings = new ArrayList();
        if( info.containsKey("mappings") ) {
            Map m = (Map)info.remove("mappings");
            for( Object o : m.entrySet()) {
                Map.Entry me = (Map.Entry)o;
                String key = me.getKey().toString();
                String value = me.getValue().toString();
                urlMappings.add( new UrlMapping(key,value));
            }
        }
    }
    
    public List<AbstractFile> getItems() {
        if(items==null) {
            items = new ArrayList();
            ExecutorService es = Executors.newFixedThreadPool(5);
            String sid = super.getFilepath();
            if( sid.endsWith("/")) sid = sid.substring(0, sid.length()-1);
            final String parentId = sid;
            
            final String _context = super.getContext();
            
            List<Future> futures = new ArrayList();
            for(final String subid: ids) {
                Future f = es.submit(  new Runnable() {
                    public void run() {
                        try {
                            items.add( getFileManager().find( parentId + subid, _context) );
                        } catch(Exception ign) {
                            System.out.println("Error reading file " + subid + ". " + ign.getMessage());
                        }
                    }
                });
                futures.add(f);
            }
            for(Future f: futures) { 
                try { f.get() ; } catch(Exception ign){;}
            }
        }
        Collections.sort( items );
        return items;
    }
    
    public List<UrlMapping> getUrlMappings() {
        return urlMappings;
    }
    
    
    public Object getContent() {
        return getItems();
    }

    public boolean hasItems() {
        return ids.size() > 0;
    }
    
}
