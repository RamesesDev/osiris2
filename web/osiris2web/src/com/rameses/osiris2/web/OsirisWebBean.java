/*
 * OsirisWebBean.java
 *
 * Created on May 26, 2010, 3:41 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.SessionContext;
import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


public class OsirisWebBean implements Serializable {
    
    private Map invokers = new InvokerLookup();
    private Map folders = new FolderLookup();
    private Map folderInvokers = new FolderInvokerLookup();
    
    public String getPath() {
        return WebContext.getInstance().getExternalContext().getRequestContextPath();
    }
    
    public OsirisUserPrincipal getUser() {
        return (OsirisUserPrincipal) WebContext.getInstance().getUserPrincipal();
    }
    
    public String getUsername() {
        Principal p = WebContext.getInstance().getUserPrincipal();
        if ( p != null )
            return p.getName();
        
        return null;
    }
    
    public Map getInvokers() {
        return invokers;
    }
    
    public Map getFolders() {
        return folders;
    }
    
    public Map getFolderInvokers() {
        return folderInvokers;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  InvokerLookup  ">
    class InvokerLookup extends HashMap {
        
        public Object get(Object key) {
            OsirisWebSessionContext ctx = (OsirisWebSessionContext) WebContext.getInstance().getSessionContext();
            return ctx.getInvokersMap( key+"" );
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  FolderLookup  ">
    class FolderLookup extends HashMap {
        
        public Object get(Object key) {
            SessionContext ctx = WebContext.getInstance().getSessionContext();
            return ctx.getFolders( key+"" );
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  InvokerLookup  ">
    class FolderInvokerLookup extends HashMap {
        
        public Object get(Object key) {
            OsirisWebSessionContext ctx = (OsirisWebSessionContext) WebContext.getInstance().getSessionContext();
            return ctx.getFolderInvokers( key+"" );
        }
        
    }
    //</editor-fold>
}
