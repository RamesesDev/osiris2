/*
 * Folder.java
 *
 * Created on March 29, 2009, 11:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class Folder implements Serializable, Comparable {
    
    private String id;
    private String fullId;
    private String caption;
    private Folder parent;
    private List folders = new ArrayList();
    private Invoker invoker;
    private Integer index = new Integer(0);
    private Map properties = new HashMap();
    
    //package level
    private boolean visible = false;
    
    
    public Folder(String id, String caption) {
        this.id = id;
        this.caption = caption;
        this.fullId = "/" + id;
    }
    
    
    public Folder(String id, String caption, Folder parent, Invoker invoker) {
        this.id = id;
        this.caption = caption;
        this.setParent(parent);
        this.fullId = getParent().getFullId() + "/" + id;
        this.invoker = invoker;
        this.index = invoker.getIndex();
        notifyVisible();
    }
    
    

    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    public String getId() {
        return id;
    }

    public String getFullId() {
        return fullId;
    }

    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Folder getParent() {
        return parent;
    }

    public List getFolders() {
        return folders;
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    public boolean isVisible() {
        return visible;
    }
    //</editor-fold>

    public boolean equals(Object object) {
        if( object == null || !(object instanceof Folder) )return false;
        Folder f = (Folder)object;
        return getFullId().equals( f.getFullId() );
    }
    
    public void removeSelf() {
        this.getParent().getFolders().remove(this);
    }

    
    public void notifyVisible() {
        this.visible = true;
        Folder p = getParent();
        while(p!=null) {
            p.visible = true;
            p = p.getParent();
        }
    }
    
    
    public String toXml() {
        StringBuffer sb = new StringBuffer();
        sb.append( "<folder id=\""+ id + "\" caption=\"" + caption + "\" visible=\"" + visible + "\"  index=\"" + index+ "\"");
        if( this.getFolders().size()>0 ) {
            sb.append(">\n");
            Iterator iter = folders.iterator();
            while(iter.hasNext()) {
                Folder f = (Folder)iter.next();
                sb.append( f.toXml() );
            }
            sb.append("</folder>\n");
        }
        else {
            sb.append("/>\n");
        }
        return sb.toString();
    }
    
    /*
    public void mergeFolders( List newFolders ) {
        Iterator iter = newFolders.iterator();
        while(iter.hasNext()) {
            Folder newFolder = (Folder)iter.next();
            
            //important! replace the parent with the old...
            newFolder.setParent(this);
            int idx = folders.indexOf(newFolder);
            if( idx >=0 ) {
                Folder old = (Folder)folders.get( idx );
                old.mergeFolders( newFolder.getFolders() );
            }
            else {
                folders.add( newFolder );
            }
        }
        Collections.sort( folders );
    }
     */

    public void setParent(Folder parent) {
        this.parent = parent;
    }
    
    public int compareTo(Object o ) {
        if( o == null || !(o instanceof Folder) ) return 0;
        Folder i = (Folder)o;
        
        int index1 = getIndex() == null ? 0 : getIndex();
        int index2 = i.getIndex() == null ? 0 : i.getIndex();
        
        return index1 - index2;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Map getProperties() {
        return properties;
    }

    public void setFullId(String fullId) {
        this.fullId = fullId;
    }
    
}
