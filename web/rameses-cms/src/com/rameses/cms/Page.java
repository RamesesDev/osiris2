/*
 * Page.java
 *
 * Created on June 19, 2012, 9:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class Page extends AbstractFile {
    
    private Map info = new HashMap();
    private String theme;
    private String master;
    private Boolean hasSubpages;
    
    /** Creates a new instance of Page */
    public Page( Map map) {
        info.putAll( map );
        this.theme = (String)info.remove("theme");
        this.master = (String)info.remove("master");
        this.setTitle((String)info.remove("title"));
        Object sorder = info.remove("sortorder");
        if(sorder!=null) {
            try {
                this.setSortorder( Integer.parseInt(sorder.toString()) );
            } catch(Exception ign){;}
        }
    }
    
    public Map getInfo() {
        return info;
    }
    
    public String getTheme() {
        return theme;
    }
    
    public String getMaster() {
        return master;
    }
    
    public boolean getHasSubpages() {
        if( hasSubpages == null ) {
            String n = this.getFilepath().substring(0, this.getFilepath().lastIndexOf("."));
            try {
                Folder folder = (Folder)getFileManager().find( n );
                hasSubpages = new Boolean( folder.hasItems() );
            } catch(Exception e) {
                hasSubpages = new Boolean(false);
            }
        }
        return hasSubpages.booleanValue();
    }
    
   
    
    //this should be called by the content parsers to ensure there will be
    //no errors just in case the field does not exist.
    public Map toMap() {
        Map map = new HashMap();
        if(info!=null) map.putAll(info);
        map.put("title", title);
        map.put("id",getId());
        map.put("filepath",getFilepath());
        map.put("pagepath",getPagepath());
        map.put("theme", theme);
        map.put("master", master);
        return map;
    }
    
}
