package com.rameses.rcp.common;


public interface ProgressListener {
    
    void onStart( int min, int max);
    void onProgress(int totalFetched, int maxSize);
    void onStop();
    void onSuspend();
    
}
