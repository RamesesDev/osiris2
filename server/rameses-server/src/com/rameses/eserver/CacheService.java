/*
 * CacheService.java
 * Created on August 17, 2011, 9:27 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.naming.InitialContext;

/**
 *
 * @author jzamss
 */
public class CacheService implements CacheServiceMBean,Serializable,Runnable {
    
    private ConcurrentHashMap<String,CacheUnit> cache = new ConcurrentHashMap();
    private ScheduledExecutorService cleaner;
    
    public void start() throws Exception {
        System.out.println("STARTING CACHE SERVICE");
        InitialContext ictx = new InitialContext();
        JndiUtil.bind( ictx, AppContext.getPath()+ CacheService.class.getSimpleName(), this );
        cleaner = Executors.newScheduledThreadPool(1);
        cleaner.scheduleWithFixedDelay(this,30,60,TimeUnit.SECONDS);
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING CACHE SERVICE");
        cleaner.shutdown();
        cache.clear();
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind( ictx, AppContext.getPath()+ CacheService.class.getSimpleName() );
    }
    
    public void put(String name, Object object, int timeout) {
        put(name, object, timeout, null);
    }

    public void put(String name, Object object, int timeout, CacheServiceListener listener) {
        CacheUnit cu = new CacheUnit(object,timeout,listener);
        cache.putIfAbsent( name, cu);
    }
    
    public Object get(String name) {
        CacheUnit cu = cache.get(name);
        if(cu!=null) {
            cu.updateExpiry();
            return cu.getObject();
        }
        
        //one should scan other cache servers. use notifier architecture
        return null;
    }
    
    public void run() {
        Iterator<String> iter = cache.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            CacheUnit cu = cache.get(key);
            if(cu.canRemove()) {
                cache.remove(key);
                CacheServiceListener listener = cu.getListener();
                if(listener!=null) listener.timeout(key, cu.getObject());
                cu.destroy();
            }
        }
    }

    public void remove(String name) {
        cache.remove(name);
    }

    
    private class CacheUnit implements Serializable {
        private Object object;
        private int timeout;
        private Date expirydate;
        private CacheServiceListener listener;
        
        public CacheUnit(Object o, int timeout, CacheServiceListener l) {
            this.object = o;
            this.timeout = timeout;
            this.listener = l;
            this.updateExpiry();
        }
        public Object getObject() {
            return object;
        }
        public void updateExpiry() {
            Calendar cal = Calendar.getInstance();
            Date d = new Date();
            cal.setTime(d);
            cal.add(Calendar.MILLISECOND, this.timeout);
            this.expirydate = cal.getTime();
        }
        public boolean canRemove() {
            Date d = new Date();
            return this.expirydate.before(d);
        }

        public CacheServiceListener getListener() {
            return listener;
        }
        
        public void destroy() {
            this.listener = null;
            this.object = null;
        }
    }
    
}
