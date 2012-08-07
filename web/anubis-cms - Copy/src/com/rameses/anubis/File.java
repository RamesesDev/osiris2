/*
 * AbstractFile.java
 *
 * Created on July 3, 2012, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class File extends HashMap implements Comparable {
    
    private String path;
    
    /** Creates a new instance of AbstractFile */
    public File(Map info) {
        super.putAll( info );
        
        Object sorder = info.get("sortorder");
        if( sorder == null) sorder = 0;
        try {sorder =  Integer.parseInt(sorder.toString()) ;} catch(Exception ign){;}
        super.put("sortorder", sorder);
        
        Object sec = info.get("secured");
        if(sec == null) sec = false;
        try { sec = Boolean.parseBoolean(sec.toString() );}catch(Exception ign){;}
        super.put("secured", sec);
        
        String _id = getId();
        String path = _id.substring(0, _id.lastIndexOf("."));
        super.put("path", path);
        
        Object ver = info.get("version");
        if( ver == null ) ver = 1.0;
        try { ver = Double.parseDouble(ver.toString() );}catch(Exception ign){;}
        super.put("version", ver);
        
        Object hid = info.get("hidden");
        if(hid == null) hid = false;
        try { hid = Boolean.parseBoolean(hid.toString() );}catch(Exception ign){;}
        super.put("hidden", hid );
        
        Object frag = info.get("fragment");
        if(frag == null) frag = false;
        try { frag = Boolean.parseBoolean(frag.toString() );}catch(Exception ign){;}
        super.put("fragment", frag );
        
        String id = getId();
        super.put("name", id.substring(1,id.lastIndexOf(".")).replace("/", "-"));
        
        super.put("pagename", id.substring( id.lastIndexOf("/")+1, id.lastIndexOf(".")) );
    }
    
    public String getId() {
        return (String)super.get("id");
    }
    
    public String getName() {
        return (String)super.get("name");
    }
    
    public String getPagename() {
        return (String)super.get("pagename");
    }
     
    public String getTitle() {
        return (String)get("title");
    }
    
    public String getHref() {
        return (String)get("href");
    }
    
    public int getSortorder() {
        return ((Integer)super.get("sortorder")).intValue();
    }
    
    public boolean isSecured() {
        return ((Boolean)super.get("secured")).booleanValue();
    }
    
    public boolean isHidden() {
        return ((Boolean)super.get("hidden")).booleanValue();
    }
    
    public boolean isFragment() {
        return ((Boolean)super.get("fragment")).booleanValue();
    }
    
    public String getExt() {
        return (String)super.get("ext");
    }
    
    public double getVersion() {
        return ((Double)super.get("version")).doubleValue();
    }
    
    public String getModule() {
        return (String)super.get("module");
    }
    
    public int compareTo(Object o) {
        File comp = (File)o;
        if( comp.getSortorder() < getSortorder() )
            return 1;
        else if( comp.getSortorder() > getSortorder()) {
            return -1;
        } else {
            return  getId().compareTo( comp.getId() );
        }
    }
    
    public String getPath() {
        return (String)super.get("path");
    }
    
    public boolean equals(Object o) {
        File comp = (File)o;
        return getId().equals( comp.getId() );
    }
    
    
    //TRANSLATED GET TEXT
    private static String IGNORE_LANG_FIELDS = "sortorder|secured|href|version|hidden|ext|path|id";
    
    public Object get(Object key) {
        if(! key.toString().matches(IGNORE_LANG_FIELDS)) {
            if(AnubisContext.getCurrentContext()!=null && AnubisContext.getCurrentContext().getCurrentLocale()!=null) {
                LocaleSupport locale = AnubisContext.getCurrentContext().getCurrentLocale();
                if(locale!=null) {
                    String _key = getPath().substring(1).replaceAll("/", ".")+"."+key;
                    String val = (String)locale.getResourceFile().get(_key);
                    if(val!=null && val.trim().length()>0) return val;
                }
            }
        }
        return super.get(key);
    }
    
    
    
}
