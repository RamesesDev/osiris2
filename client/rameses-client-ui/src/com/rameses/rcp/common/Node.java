/*
 * Node.java
 *
 * Created on January 10, 2010, 7:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.common;

import java.rmi.server.UID;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class Node {
    
    private String id = "NODE" + new UID();
    private Object item;
    private String caption;
    private String tooltip;
    private boolean dynamic;
    private boolean leaf;
    private String icon;
    private Map properties;
     private boolean loaded;
    
    public Node() {
    }
    
    public Node(String id) {
        this.id = id;
    }
    
    public Node(String id, String caption) {
        this.id = id;
        this.caption = caption;
    }
    
    public Node(String id, String caption, Object o) {
        this.id = id;
        this.caption = caption;
        this.item = o;
    }
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    
    public Object getItem() {
        return item;
    }
    
    public void setItem(Object object) {
        this.item = object;
    }
    
    public String getCaption() {
        if(caption ==null) return id;
        return caption;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public String getTooltip() {
        return tooltip;
    }
    
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    
    public boolean isLeaf() {
        return leaf;
    }
    
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    //</editor-fold>

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Map getProperties() {
        return properties;
    }
    
     public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
