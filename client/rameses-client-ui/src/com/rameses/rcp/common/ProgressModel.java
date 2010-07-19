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
    private int estimatedMaxSize;
    private int totalFetchedSize;
    
    private ProgressInfo progressInfo;

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
        totalFetchedSize = 0;
        estimatedMaxSize = progressInfo.getEstimatedMaxSize();
        for(ProgressListener l: listeners) {
            l.onStart(minSize, estimatedMaxSize );
        }
    }
    
    public void notifySuspend() {
        for(ProgressListener l: listeners) {
            l.onSuspend();
        }
    }
    
    public void notifyStop() {
        for(ProgressListener l: listeners) {
            l.onStop();
        }
    }

    public void addCompleted(int newSize) {
        totalFetchedSize += newSize;
        if( totalFetchedSize >= estimatedMaxSize) {
            estimatedMaxSize = progressInfo.getEstimatedMaxSize();
        }
        for(ProgressListener l: listeners) {
            l.onProgress(totalFetchedSize, estimatedMaxSize);
        }
    }

    public void setValue(int sz) {
        totalFetchedSize = sz;
        if( totalFetchedSize >= estimatedMaxSize) {
            estimatedMaxSize = progressInfo.getEstimatedMaxSize();
        }
        for(ProgressListener l: listeners) {
            l.onProgress(totalFetchedSize, estimatedMaxSize);
        }
    }
    
    public int getTotalFetchedSize() {
        return totalFetchedSize;
    }
    
    public boolean isCompleted() {
        return   totalFetchedSize>=estimatedMaxSize ;
    }
    
}
