/*
 * DefaultCacheContent.java
 * Created on September 28, 2011, 10:37 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import com.rameses.service.EJBServiceContext;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class RemoteCacheUnit extends CacheUnit {
    
    private CacheServiceMBean cacheService;
    private Map hostInfo;
    
    public RemoteCacheUnit(String name, Map hostInfo) {
        super(name);
        EJBServiceContext ctx = new EJBServiceContext(hostInfo);
        cacheService = ctx.create("CacheService", CacheServiceMBean.class);
        this.hostInfo = hostInfo;
    }
    
    public Object getContent() {
        try {
            Object info = cacheService.getLocalContent(name);
            super.updateExpiry();
            if(info==null)
                throw new NullPointerException();
            return info;
        }
        catch(Exception e) {
            throw new RemoteCacheLostException();
        }
    }
    
    public void updateContent(Object info) {
        super.updateExpiry();
        cacheService.updateLocalCache(name, info);
    }
    
    public boolean destroy(boolean timedout) {
        try {
            cacheService.removeLocalCache(name,timedout);
            hostInfo = null;
            cacheService = null;
            return true;
        } catch(Exception e) {
            System.out.println("error removing remote cache. " + name);
            return false;
        }
    }
    
    public String toString() {
        return super.toString() + " REMOTE " + hostInfo;
    }
}
