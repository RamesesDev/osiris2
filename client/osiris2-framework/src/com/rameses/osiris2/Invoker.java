/*
 * IInvoker.java
 *
 * Created on February 23, 2009, 9:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class Invoker implements Serializable, Comparable {
    
    private String workunitid;
    private String workunitname;
    private String name;
    private String caption;
    private String ref;
    private String type = "folder";
    private String permission;
    private Integer index;
    private String action;
    private Map properties = new HashMap();
    private String roles;
    private Module module;
    
    public Invoker() {
        
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permissionKey) {
        this.permission = permissionKey;
    }

    public Map getProperties() {
        return properties;
    }
    
    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append( getType() +":"+getWorkunitid() );
        if( this.getName()!=null ) {
            sb.append( "_"+getName() );
        }
        if(this.getAction()!=null) {
            sb.append("_"+getAction());
        }
        return sb.toString().hashCode();
    }

    public boolean equals(Object object) {
        if( object == null || !(object instanceof Invoker)) return false;
        Invoker i = (Invoker)object;
        return hashCode() == i.hashCode();
    }

    public String getCaption() {
        if( caption == null )
            if( name !=null)
                return name;
            else
                return "[No Caption]";
        else            
            return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public int compareTo(Object o ) {
        if( o == null || !(o instanceof Invoker) ) return 0;
        Invoker i = (Invoker)o;
        int aidx = getIndex() == null? 0 : getIndex();
        int bidx = i.getIndex() == null? 0 : i.getIndex();
        
        return aidx - bidx;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getWorkunitid() {
        return workunitid;
    }

    public void setWorkunitid(String workunitid) {
        this.workunitid = workunitid;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getWorkunitname() {
        return workunitname;
    }

    public void setWorkunitname(String workunitname) {
        this.workunitname = workunitname;
    }

   

    
}
