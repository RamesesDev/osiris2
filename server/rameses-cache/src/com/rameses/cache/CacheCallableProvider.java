/*
 * CallableCacheInvoker.java
 * Created on September 28, 2011, 9:00 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import java.util.Map;
import java.util.concurrent.Callable;


public interface CacheCallableProvider {
    
    Callable getCallable(String name, Map conf);
    
}
