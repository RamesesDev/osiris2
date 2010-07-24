/*
 * RolePermission.java
 *
 * Created on April 1, 2010, 12:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.auth.server;

import java.io.Serializable;

/**
 *
 * @author elmo
 */
public class Permission implements Serializable {
    
    private String name;
    private String title;
    private boolean allowed;
    
    public Permission(String name, String title,boolean allow) {
        setName(name);
        setTitle(title);
        setAllowed(allow);
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

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
    
}
