/*
 * PageInstance.java
 *
 * Created on July 1, 2012, 6:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Elmo
 */
public class PageFileInstance extends FileInstance {
    
    private Set scripts = new LinkedHashSet();
    private Set styles = new LinkedHashSet();
    private Set tags = new LinkedHashSet();
    private Map vars = new HashMap();
    
    private Map processQueue = new HashMap();
    private Set imports = new LinkedHashSet();
    
    //private String master;
    //private String theme;
    
    private PageFileHandler fileHandler;
    
    /** Creates a new instance of PageInstance */
    PageFileInstance(File file, PageFileHandler handler) {
        super( file );
        this.fileHandler = handler;
    }
    
    public InputStream getContent() {
        PageContentMap map = new PageContentMap(this, getParams(), fileHandler );
        String[] arrs = (getMaster()+",default").split(",");
        String result = null;
        
        for(int i=0; i<arrs.length;i++) {
            try {
                String _master = arrs[i].trim();
                result = fileHandler.getMasterContentProvider().getContent(_master,this,map);   
                if(result!=null) break;
            }
            catch(Exception e) {
               if(i >=arrs.length ) 
                   throw new RuntimeException("Theme master not found");
            }
        }
        
        //replace the processed queue;
        Map queue = this.getProcessQueue();
        for(Object o : queue.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            Object[] res = (Object[])me.getValue();
            String widgetName = (String) res[0];
            Map options = (Map)res[1];
            //PageContentMap pmap = new PageContentMap( pi, pi.getParams(), map.getContentHandler(), map.getFileHandler() );
            map.put("OPTIONS", options);
            result = result.replaceFirst(  me.getKey().toString(), fileHandler.getWidgetContentProvider().getContent(widgetName,this,map));
        }        
        return new ByteArrayInputStream( result.getBytes() );
    }
    
    public String getMaster() {
        //if this is module, check the modules master
        String master = (String)getFile().get("master");
        if( master !=null ) return master;
        if( getFile().isFragment()) {
            return "fragment";
        }
        if( getModule()!=null) {
            master = getModule().getDefaultMaster();
            if( master !=null) return master;
        }
        return "default";
    }
    
    public Theme getTheme() {
        String themeName = (String)getFile().get("theme");
        Theme theme = null;
        if( themeName !=null) {
            theme = getProject().getThemes().get(themeName);
            if(theme!=null) return theme;
        }
        if(getModule()!=null) {
            theme = getModule().getDefaultTheme();
            if(theme!=null) return theme;
        }
        theme = getProject().getDefaultTheme();
        if(theme!=null) return theme;
        return getProject().getSystemTheme();
     }    
    
    
    public Set getScripts() {
        return scripts;
    }
    
    public Set getStyles() {
        return styles;
    }
    
    public Set getImports() {
        return imports;
    }
    
    public Set getTags() {
        return tags;
    }
    
    public Map toMap() {
        Map map = new HashMap() {
            public Object get(Object key) {
                if(!super.containsKey(key)) { 
                    return "";
                }
                return super.get(key);
            }
        };
        map.putAll( getFile() );
        map.put( "id", getId());
        map.put( "title", getTitle());
        map.put("tags", getTags());
        map.put("href", getHref());
        map.put("scripts", getScripts());
        map.put("styles", getStyles());
        map.put("imports", getImports());
        map.put("theme", getTheme().getName());
        map.put("secured", isSecured() );
        return map;    
    }

    public Map getVars() {
        return vars;
    }

    public Map getProcessQueue() {
        return processQueue;
    }

    
}
