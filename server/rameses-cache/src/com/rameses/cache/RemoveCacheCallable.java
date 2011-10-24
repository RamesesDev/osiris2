/*
 * RemoteCacheProxyCacheCallable.java
 * Created on September 28, 2011, 9:12 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 *
 * @author jzamss
 */
public class RemoveCacheCallable implements CacheCallableProvider {
    
    private boolean timedout;
    
    public RemoveCacheCallable(boolean timedout ) {
        this.timedout = timedout;
    }
    
    public Callable getCallable(String name, Map conf) {
        return new DefaultImpl(name, conf);
    }
    
    public class DefaultImpl implements Callable {
        private RemoteCacheUnit remote;
        
        /** Creates a new instance of CacheProxyExecutor */
        public DefaultImpl(String name, Map hostInfo) {
            this.remote = new RemoteCacheUnit(name, hostInfo);
        }
        /***
         * attempt to contact the remote cache. This is done by testing
         * the cacheproxy get method. If it returns null or it has an error
         * we should ignore it. Otherwise return an object array containing
         * the proxy and the result. The proxy will be stored for future use.
         */
        public Object call() throws Exception {
            try {
                remote.destroy(timedout);
            } catch(Exception ign) {;}
            //do nothing
            return null;
        }
        
    }
    
}
