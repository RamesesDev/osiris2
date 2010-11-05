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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private Map properties = new HashMap();
    private boolean loaded;
    private String mnemonic;
    
    private List<NodeListener> listeners = new ArrayList();
    
    
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
    
    public void addListener(NodeListener listener) {
        if ( !listeners.contains(listener) ) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(NodeListener listener) {
        listeners.remove(listener);
    }
    
    public void reload() {
        for (NodeListener nl: listeners) {
            nl.reload();
        }
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
    //</editor-fold>

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }
    
}
