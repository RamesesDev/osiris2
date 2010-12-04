/*
 * WebBrowserModel.java
 *
 * Created on December 2, 2010, 12:48 PM
 * @author jaycverg
 */

package com.rameses.rcp.common;

import com.rameses.util.ValueUtil;
import java.util.Stack;

public class WebBrowserModel {
    
    private String homeLocation;
    private String location;
    private String baseUrl;
    private int maxHistorySize = 20;
    private Listener listener;
    
    private Stack<String> history = new Stack();
    private Stack<String> forwardHistory = new Stack();
    
    
    public WebBrowserModel() {}
    
    public WebBrowserModel(String location) {
        setLocation(location);
    }
    
    public void back() {
        if ( !history.isEmpty() ) {
            addForwardHistory( location );
            location = history.pop();
            updateBaseUrl();
            refresh();
        }
    }
    
    public void forward() {
        if ( !forwardHistory.isEmpty() ) {
            addHistory( location );
            location = forwardHistory.pop();
            updateBaseUrl();
            refresh();
        }
    }
    
    public void refresh() {
        if ( listener != null )
            listener.refresh();
    }
    
    public void clear() {
        history.clear();
        forwardHistory.clear();
        location = homeLocation;
        updateBaseUrl();
        refresh();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void addHistory(String location) {
        if ( location == null ) return;
        
        history.push(location);
        if ( history.size() > maxHistorySize) {
            history.setSize(maxHistorySize);
        }
    }
    
    private void addForwardHistory(String location) {
        if ( location == null ) return;
        
        forwardHistory.push(location);
        if ( forwardHistory.size() > maxHistorySize) {
            forwardHistory.setSize(maxHistorySize);
        }
    }
    
    private void updateBaseUrl() {
        baseUrl = location.substring(0, location.lastIndexOf("/")+1);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  getters/setters  ">
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        if ( location == null ) return;
        
        location = location.replace("\\", "/");
        if ( !ValueUtil.isEqual(this.location, location) ) {
            if ( this.location != null )
                addHistory( this.location ); //push the old location
            
            this.location = location;
            updateBaseUrl();
            
            if ( homeLocation == null ) {
                homeLocation = this.location;
            }
            refresh();
        }
    }
    
    public void setRelativeLocation(String location) {
        setLocation( getBaseUrl() + location );
    }
    
    public boolean isCanBack() {
        return !history.isEmpty();
    }
    
    public boolean isCanForward() {
        return !forwardHistory.isEmpty();
    }
    
    public int getMaxHistorySize() {
        return maxHistorySize;
    }
    
    public void setMaxHistorySize(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
    }
    
    public Listener getListener() {
        return listener;
    }
    
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  Listener (interface)  ">
    public static interface Listener {
        
        void refresh();
        
    }
    //</editor-fold>
    
}
