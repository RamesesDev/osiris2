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
        HttpServletRequest req = WebContext.getRequest();
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
            return WebContext.getRequest().getSession().getId();
        }
        
        public Object get(Object key) {
            return WebContext.getRequest().getSession().getAttribute((String) key);
        }
        
        public void put(Object key, Object value) {
            WebContext.getRequest().getSession().setAttribute((String) key, value);
        }
        
        public void remove(Object key) {
            WebContext.getRequest().getSession().removeAttribute((String) key);
        }
        
    }
    
}
