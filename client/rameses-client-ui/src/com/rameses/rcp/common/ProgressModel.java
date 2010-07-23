/*
 * ProgressModel.java
 *
 * Created on January 29, 2010, 10:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.common;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmo
 */
public class ProgressModel {
    
    private int minSize = 0;
    private long estimatedMaxSize;
    private long totalFetchedSize;
    private boolean started;
    
    private ProgressInfo progressInfo;
    
    
    public ProgressModel() {}
    
    public ProgressModel(ProgressInfo info) {
        this.progressInfo = info;
    }
        
    private List<ProgressListener> listeners = new ArrayList<ProgressListener>();
    
    public void addListener( ProgressListener listener ) {
        listeners.add(listener);
    }
    
    public void removeListener( ProgressListener listener ) {
        listeners.remove(listener);
    }
    
    public void notifyStart() {
        if ( started ) return;
        
        totalFetchedSize = 0;
        estimatedMaxSize = getEstimatedMaxSize();
        for(ProgressListener l: listeners) {
            l.onStart(minSize, 100 );
        }
        started = true;
    }
    
    public void notifySuspend() {
        for(ProgressListener l: listeners) {
            l.onSuspend();
        }
        started = false;
    }
    
    public void notifyStop() {
        if ( !started ) return;
        
        for(ProgressListener l: listeners) {
            l.onStop();
        }
        started = false;
    }

    public void addCompleted(long newSize) {
        totalFetchedSize += newSize;
        if( totalFetchedSize >= estimatedMaxSize) {
            estimatedMaxSize = getEstimatedMaxSize();
        }
        for(ProgressListener l: listeners) {
            l.onProgress(getComputedValue(), 100);
        }
    }

    public void setValue(long sz) {
        totalFetchedSize = sz;
        if( totalFetchedSize >= estimatedMaxSize) {
            estimatedMaxSize = getEstimatedMaxSize();
        }
        for(ProgressListener l: listeners) {
            l.onProgress(getComputedValue(), 100);
        }
    }
    
    private int getComputedValue() {
        int count = (int) ((totalFetchedSize / (double) getEstimatedMaxSize()) * 100);
        return count;
    }
    
    public long getTotalFetchedSize() {
        return totalFetchedSize;
    }
    
    public boolean isCompleted() {
        return   totalFetchedSize>=estimatedMaxSize ;
    }

    public long getEstimatedMaxSize() {
        if ( progressInfo != null )
            return progressInfo.getEstimatedMaxSize();
        
        return estimatedMaxSize;
    }

    public void setEstimatedMaxSize(long estimatedMaxSize) {
        this.estimatedMaxSize = estimatedMaxSize;
    }

    public boolean isStarted() {
        return started;
    }
    
}
