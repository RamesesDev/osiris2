/*
 * DefaultCacheContent.java
 * Created on September 28, 2011, 10:37 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.cache;

import com.rameses.util.ChangeQueue;

/**
 *
 * @author jzamss
 */
public class LocalCacheUnit extends CacheUnit implements ChangeQueue.Handler {
    
    private Object info;
    private CacheListener listener;
    private ChangeQueue queue = new ChangeQueue(this);
    private CacheUpdateHandler updateHandler;
    
    public LocalCacheUnit( String name, Object info, CacheUpdateHandler updateHandler) {
        super(name);
        this.info = info;
        this.updateHandler = updateHandler;
    }
    
    public void setListener(CacheListener listener) {
        this.listener = listener;
    }
    
    //update the expiry everytime object is accessed
    public Object getContent() {
        super.updateExpiry();
        return info;
    }
    
    public void updateContent(Object updateInfo) {
        queue.push( updateInfo );
        queue.applyChanges();
    }
    
    //called after updating the changes.
    public void onAfterUpdate() {
        if(listener!=null) {
            listener.updated( name, info );
        }
    }
    
    
    public boolean destroy(boolean timedout) {
        //fire the listener.
        if(this.listener!=null) {
            if(timedout)
                this.listener.timeout(name, info);
            else
                this.listener.removed(name, info);
        }
        info = null;
        this.listener = null;
        return true;
    }
    
    public String toString() {
        return super.toString() + " LOCAL " + info;
    }
    //called by ChangeQueue
    public void update(Object data) {
        if(updateHandler!=null)
            updateHandler.update(super.getName(), info, data);
        else
            throw new RuntimeException("Update Handler is not set");
    }
    
    
    
}
