/*
 * RolePermission.java
 *
 * Created on April 1, 2010, 12:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

import java.io.Serializable;

/**
 *
 * @author elmo
 */
public class Permission implements Serializable {
    
    private String name;
    private String title;
    private String category;
    
    public Permission(String name, String title) {
        setName(name);
        setTitle(title);
    }

    
    public Permission() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return name +"=" + title;
    }

    public boolean equals(Object obj) {
        if(obj==null) return false;
        Permission p = (Permission)obj;
        return getName().equals( p.getName() );
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    

            
    
}
