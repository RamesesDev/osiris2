/*
 * AbstractAction.java
 *
 * Created on October 19, 2009, 7:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.common;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Action implements Comparable {
    
    private String name;
    //index is used for sorting
    private int index;
    private Action parent;
    private String caption;
    private String category;
    private String icon;
    private char mnemonic = ' ';
    private boolean immediate;
    private Map properties = new Hashtable();
    private String permission;
    private Map parameters = new HashMap();
    private boolean update;
    private String tooltip;
    private String visibleWhen;
    
    public Action() {
        
    }
    
    public Action(String name) {
        this.name = name;
    }
    public Action(String name, String caption, String icon ) {
        this.name = name;
        this.caption = caption;
        this.icon = icon;
    }
    public Action(String name, String caption, String icon, char mnemonic ) {
        this.name = name;
        this.caption = caption;
        this.icon = icon;
        if(mnemonic!=' ') this.mnemonic = mnemonic;
    }
    
    public Action(String name, String caption, String icon, char mnemonic, String perm ) {
        this.name = name;
        this.caption = caption;
        this.icon = icon;
        this.mnemonic = mnemonic;
        this.permission = perm;
    }
    
    public int compareTo(Object o) {
        if( o == null ) return 0;
        if(!(o instanceof Action)) return 0;
        
        Action a = (Action)o;
        return index - a.index;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public Action getParent() {
        return parent;
    }
    
    public void setParent(Action parent) {
        this.parent = parent;
    }
    
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getPath() {
        //path will be the name of the category plus its name.
        return category + "/" + name;
    }
    
    public Map getProperties() {
        return properties;
    }
    
    public char getMnemonic() {
        return mnemonic;
    }
    
    public void setMnemonic(char mnemonic) {
        this.mnemonic = mnemonic;
    }
    
    public boolean isImmediate() {
        return immediate;
    }
    
    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    public Map getParameters() {
        return parameters;
    }
    
    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }
    
    public boolean isUpdate() {
        return update;
    }
    
    public void setUpdate(boolean update) {
        this.update = update;
    }
    
    public String getTooltip() {
        if(tooltip==null) {
            if( caption == null )
                return name;
            else
                return caption;
        }
        
        return tooltip;
    }
    
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
    
    public String getVisibleWhen() {
        return visibleWhen;
    }
    
    public void setVisibleWhen(String visibleWhen) {
        this.visibleWhen = visibleWhen;
    }
    //</editor-fold>
    
}
