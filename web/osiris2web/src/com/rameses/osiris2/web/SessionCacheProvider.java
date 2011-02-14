/*
 * SessionCacheProvider.java
 *
 * Created on June 28, 2010, 9:16 AM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.common.CacheProvider;
import javax.servlet.http.HttpServletRequest;


public class SessionCacheProvider extends CacheProvider {
    
    private CacheProvider.CacheContext getContext() {
        HttpServletRequest req = WebContext.getInstance().getRequest();
        SessionCacheContext ctx = (SessionCacheContext) req.getAttribute(SessionCacheContext.class.getName());
        if ( ctx == null ) {
            ctx = new SessionCacheContext();
            req.setAttribute(SessionCacheContext.class.getName(), ctx);
        }
        return ctx;
    }
    
    
    public CacheProvider.CacheContext createContext() {
        return getContext();
    }
    
    public CacheProvider.CacheContext getContext(String id) {
        return getContext();
    }
    
    public CacheProvider.CacheContext removeContext(String id) {
        return null;
    }
    
    
    public static class SessionCacheContext extends CacheProvider.CacheContext {
        
        public String getId() {
            return WebContext.getInstance().getRequest().getSession().getId();
        }
        
        public Object get(Object key) {
            return WebContext.getInstance().getRequest().getSession().getAttribute((String) key);
        }
        
        public void put(Object key, Object value) {
            WebContext.getInstance().getRequest().getSession().setAttribute((String) key, value);
        }
        
        public void remove(Object key) {
            WebContext.getInstance().getRequest().getSession().removeAttribute((String) key);
        }
        
    }
    
}
