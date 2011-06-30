/*
 * ObjectPool.java
 * Created on June 29, 2011, 7:18 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author jzamss
 */
public final class ObjectPool {
    
    private LinkedBlockingQueue<PooledItem> pool = new LinkedBlockingQueue();
    private Date lastAccessed = new Date();
    private PoolSource poolSource;
    private int minSize = 5;
    private int maxSize = 100;
    
    private ScheduledExecutorService cleaner = Executors.newScheduledThreadPool(1);
    
    public ObjectPool(PoolSource source) {
        this(source, 5);
    }
    public ObjectPool(PoolSource source,int minSize) {
        this(source,5,100);
    }
    
    public ObjectPool(PoolSource source,int minSize,int maxSize) {
        this.poolSource = source;
        if(minSize>0)this.minSize = minSize;
        if(maxSize>minSize) {
            this.maxSize = maxSize;
        }
        else {
            this.maxSize = minSize + 100;
        }
        init();
        //start the cleaner
        cleaner.scheduleWithFixedDelay(new Cleaner(),60,60,TimeUnit.SECONDS);
    }

    /**
     * this must be called when not using the pool to terminate threads
     */
    public void destroy() {
        cleaner.shutdown();
        this.poolSource = null;
        
    }
    
    private void init() {
        for( int i=0; i<minSize; i++) {
            this.pool.add( new PooledItem(this, poolSource.createObject()));
        }
        this.lastAccessed = new Date();
    }
    
    /**
     * returns the estimated available size
     */
    public int getPoolSize() {
        return pool.size();
    }
    
    public Object request() {
        PooledItem pi = pool.poll();
        if(pi==null) {
            pi = new PooledItem(this, poolSource.createObject());
        }
        return pi;
    }
    
    boolean sendBackToPool(PooledItem item) {
        this.lastAccessed = new Date();
        if(pool.size()>maxSize) {
            item.destroy();
            return false;
        } else {
            boolean added =  pool.offer( item );
            if(!added) {
                item.destroy();
                return false;
            }
            else {
                return true;
            }
        }
    }
    
    public boolean remove(PooledItem item) {
        return pool.remove( item );
    }
    
    private class Cleaner implements Runnable {
        public void run() {
            try {
                Date lastAccessed = ObjectPool.this.lastAccessed;
                Date today = new Date();
                long diff = DateUtil.diff(lastAccessed, today,Calendar.SECOND);
                if(diff > 60 && pool.size()>minSize ) {
                    System.out.println("start cleanup mainetenance");
                    //run the cleaner.
                    List<PooledItem> deadPool = new Vector();
                    pool.drainTo(deadPool);
                    init();
                    for(PooledItem pi: deadPool) {
                        pi.destroy();
                    }
                }
            }
            catch(Exception ign){
                System.out.println("cleanup error");
            }
        }
    }
    
    public static interface PoolSource {
        Object createObject();
    }
    
    public static final class PooledItem {
        
        private ObjectPool manager;
        private Object pooledObject;
        
        PooledItem(ObjectPool manager, Object pooledObj) {
            this.pooledObject = pooledObj;
            this.manager = manager;
        }
        
        /**
         * attempts to send this pooled item to the pool. returns true if successful
         */
        public boolean close() {
            return manager.sendBackToPool(this);
        }
        
        public void destroy() {
            this.manager = null;
            this.pooledObject = null;
        }
        
        /**
         * this method is used if you want to remove self from the object
         */
        public void removeFromPool() {
            if( this.manager.remove(this)) {
                this.destroy();
            }
        }
    }
    
    
}
