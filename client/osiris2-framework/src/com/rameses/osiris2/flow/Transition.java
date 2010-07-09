package com.rameses.osiris2.flow;

import java.util.HashMap;
import java.util.Map;

public class Transition {
    
    private String to;
    private String cond;
    private String name;
    private String confirm;
    private String action;
    private String permission;
    private String rendered;
    private Map properties = new HashMap();
    
    public Transition() {
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public String getCond() {
        return cond;
    }
    
    public void setCond(String cond) {
        this.cond = cond;
    }
    
    
    public String getName() {
        if( name == null )
            return getTo();
        else
            return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
    // </editor-fold>

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append( "[Name: " + this.getName() + "][To:" + this.getTo() + "][cond:" + this.getCond() + "]" );
        return sb.toString();
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Map getProperties() {
        return properties;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRendered() {
        return rendered;
    }

    public void setRendered(String rendered) {
        this.rendered = rendered;
    }

   

   
}
