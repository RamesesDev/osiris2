/*
 * OsirisWebSessionContext.java
 *
 * Created on June 12, 2010, 10:16 AM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.AppContext;
import com.rameses.osiris2.Folder;
import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OsirisWebSessionContext extends SessionContext {
    
    protected Map invokersMap = new HashMap();
    protected Map folderInvokers = new HashMap();
    
    private Map properties = new HashMap();
    
    
    protected OsirisWebSessionContext(AppContext ctx) {
        super(ctx);
    }
    
    public void resetInvokers() {
        super.invokers.clear();
        super.folderIndex.clear();
        invokersMap.clear();
        folderInvokers.clear();
    }
    
    public List getInvokersMap(String type) {
        if ( !invokersMap.containsKey(type) ) {
            String ctxPath = WebContext.getExternalContext().getRequestContextPath();
            List list = new ArrayList();
            for ( Object o: getInvokers(type)) {
                Invoker inv = (Invoker) o;
                StringBuffer uri = new StringBuffer();
                uri.append(ctxPath);
                uri.append("/" + inv.getWorkunitid().replace(":", "/"));
                if ( inv.getAction() != null ) {
                    uri.append("/" + inv.getAction());
                }
                uri.append(WebContext.PAGE_SUFFIX);
                
                Map m = new HashMap();
                m.put("caption", inv.getCaption());
                m.put("name", inv.getName());
                m.put("permission", inv.getPermission());
                m.put("path", uri.toString());
                m.putAll(inv.getProperties());
                list.add(m);
            }
            invokersMap.put(type, list);
        }
        
        return (List) invokersMap.get(type);
    }
    
    public List getFolderInvokers(String folderId) {
        if ( !folderInvokers.containsKey(folderId) ) {
            List list = new ArrayList();
            for (Object obj: getFolders(folderId)) {
                Folder f = (Folder) obj;
                if ( f.getInvoker() != null ) {
                    list.add(f.getInvoker());
                }
            }
            folderInvokers.put(folderId, list);
        }
        
        return (List) folderInvokers.get(folderId);
    }

    public Map getProperties() {
        return properties;
    }
    
}
