/*
 * InvokerFunctions.java
 *
 * Created on June 17, 2010, 2:28 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web.util;

import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.web.WebContext;

public class InvokerFunctions {
    
    
    public static String resourcePath( String path ) {
        String ctxPath = WebContext.getExternalContext().getRequestContextPath();
        if ( path.startsWith("/") )
            return ctxPath + path;
        
        return ctxPath + "/" + path;
    }
    
    public static String invokerPath( Invoker inv ) {
        String ctxPath = WebContext.getExternalContext().getRequestContextPath();
        StringBuffer uri = new StringBuffer(ctxPath);
        uri.append("/" + inv.getWorkunitid().replace(":", "/"));
        if ( inv.getAction() != null ) {
            uri.append("/" + inv.getAction());
        }
        uri.append(WebContext.PAGE_SUFFIX);
        return uri.toString();
    }
}