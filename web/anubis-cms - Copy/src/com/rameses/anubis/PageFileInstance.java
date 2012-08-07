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
    
    private String master;
    //private String theme;
    
    private PageFileHandler fileHandler;
    
    /** Creates a new instance of PageInstance */
    PageFileInstance(File file, PageFileHandler handler) {
        super( file );
        this.fileHandler = handler;
        
        //if this is module, check the modules master
        this.master = (String)file.get("master");
        if( master ==null ) {
            if( file.isFragment()) {
                this.master = "fragment";
            } else if( getModule()!=null) {
                master = getModule().getDefaultMaster();
            }
            if(master==null) this.master = "default";
        }
    }
    
    /**
     * There are two types of templates processed in the ff order:
     * page templates -> master templates
     * Page templates - matches the same name of the page in the theme/masters
     * Master Templates - main templates
     * Both templates exhibit inheritance styles based on the filename and
     * filename is read from right to left. For example consider a page path
     * /about/history. The matching page template for this would be about_history
     * It looks and merges templates for about_history then about.
     */
    public InputStream getContent() {
        PageContentMap map = new PageContentMap(this, getParams(), fileHandler );
        
        //check first for page templates
        String path = this.getFile().getPath();
        if(path.startsWith("/")) path = path.substring(1);
        while(true) {
            String template = path.replace("/", "_");
            try {
                String tresult = fileHandler.getMasterContentProvider().getContent(template,this,map);
                if(tresult!=null && tresult.trim().length()>0) {
                    map.put("content", tresult);
                }
            } catch(Exception e) {
                //System.out.println("template error->"+template + " "+e.getMessage());
            }
            if( path.lastIndexOf("/") <=0 ) break;
            path = path.substring(0, path.lastIndexOf("/"));
        }
        
        //check the master templates
        String result = null;
        String[] fallback = getMaster().split(",");
        for(int i=0; i<fallback.length;i++) {
            path = fallback[i].trim();
            String tresult = null;
            while(true) {
                String _master = path;
                try {
                    tresult = fileHandler.getMasterContentProvider().getContent(_master,this,map);
                    if(tresult!=null && tresult.trim().length()>0) {
                        map.put("content", tresult);
                    }
                } catch(Exception ign){;}
                if( path.lastIndexOf("_") <=0 ) break;
                path = path.substring(0, path.lastIndexOf("_"));
            }
            if(tresult!=null) {
                result = tresult;
                break;
            }
        }
        if(result == null)
            throw new RuntimeException("No master found");
        
        //replace the processed queue;
        Map queue = this.getProcessQueue();
        for(Object o : queue.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            Object[] res = (Object[])me.getValue();
            String widgetName = (String) res[0];
            Map options = (Map)res[1];
            map.put("OPTIONS", options);
            result = result.replaceFirst(  me.getKey().toString(), fileHandler.getWidgetContentProvider().getContent(widgetName,this,map));
        }
        return new ByteArrayInputStream( result.getBytes() );
    }
    
    public String getMaster() {
        return master;
    }
    
    public void setMaster(String master) {
        this.master = master;
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
    
    
    private Map localMap;
    public Map toMap() {
        if( localMap != null) return localMap;
        localMap = new HashMap() {
            public Object get(Object key) {
                if(!super.containsKey(key)) {
                    return "";
                } else if(key.equals("master")) {
                    return getMaster();
                }
                return super.get(key);
            }
            
            //except for lists and sets and master attribute, we should not allow
            //updating of page properties
            public Object put(Object key, Object value) {
                if(key.equals("master")) {
                    if( value!=null) {
                        setMaster(value.toString());
                        return super.put(key,value);
                    }
                    return super.get("master");
                }
                return super.put(key,value);
            }
        };
        localMap.putAll( getFile() );
        localMap.put( "id", getId());
        localMap.put( "title", getTitle());
        localMap.put("tags", getTags());
        localMap.put("href", getHref());
        localMap.put("scripts", getScripts());
        localMap.put("styles", getStyles());
        localMap.put("imports", getImports());
        localMap.put("theme", getTheme().getName());
        localMap.put("secured", isSecured() );
        localMap.put("master", getMaster() );
        localMap.put("name", getName() );
        localMap.put("pagename", getPagename() );
        return localMap;
    }
    
  
    
    public Map getVars() {
        return vars;
    }
    
    public Map getProcessQueue() {
        return processQueue;
    }
    
    
}
