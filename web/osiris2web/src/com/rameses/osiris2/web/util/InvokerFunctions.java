/*
 * InvokerFunctions.java
 *
 * Created on June 17, 2010, 2:28 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web.util;

import com.rameses.osiris2.Folder;
import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.web.OsirisWebSessionContext;
import com.rameses.osiris2.web.WebContext;
import java.util.List;

public class InvokerFunctions {
    
    
    public static String resourcePath( String path ) {
        String ctxPath = WebContext.getInstance().getExternalContext().getRequestContextPath();
        if ( path.startsWith("/") )
            return ctxPath + path;
        
        return ctxPath + "/" + path;
    }
    
    public static String invokerPath( Invoker inv ) {
        String ctxPath = WebContext.getInstance().getExternalContext().getRequestContextPath();
        StringBuffer uri = new StringBuffer(ctxPath);
        uri.append("/" + inv.getWorkunitid().replace(":", "/"));
        if ( inv.getAction() != null ) {
            uri.append("/" + inv.getAction());
        }
        uri.append(WebContext.PAGE_SUFFIX);
        return uri.toString();
    }
    
    public static boolean hasIcon(Folder folder) {
        return folder.getProperties().get("icon") != null;
    }
    
    public static boolean hasInvokers(Folder folder) {
        try {
            OsirisWebSessionContext sc = (OsirisWebSessionContext) WebContext.getInstance().getSessionContext();
            
            //if has invokers return true
            if( !sc.getFolderInvokers(folder.getFullId()).isEmpty() ) return true;
            
            //otherwise check recursively
            List<Folder> folders = sc.getFolders(folder);
            for(Folder f : folders) {
                if( hasInvokers(f) ) return true;
            }
        } catch(Exception e) {;}
        
        return false;
    }
    
    public static boolean checkPermission(String permissionKey) {
        try {
            OsirisWebSessionContext sc = (OsirisWebSessionContext) WebContext.getInstance().getSessionContext();
            return sc.getSecurityProvider().checkPermission(permissionKey);            
        } catch(Exception e) {;}
        
        return false;
    }
    
    public static boolean checkPermission(Invoker inv) {
        String perm = inv.getPermission();
        if( perm == null || perm.trim().length() == 0 ) return true;
        
        return checkPermission(inv.getWorkunitid() + "." + perm);
    }
}