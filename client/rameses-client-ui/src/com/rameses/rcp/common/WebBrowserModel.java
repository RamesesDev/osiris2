/*
 * WebBrowserModel.java
 *
 * Created on December 2, 2010, 12:48 PM
 * @author jaycverg
 */

package com.rameses.rcp.common;

import com.rameses.rcp.control.webbrowser.BrowserUtil;
import com.rameses.util.ValueUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

public class WebBrowserModel {
    
    private URL homeLocation;
    private URL location;
    private int maxHistorySize = 20;
    private Listener listener;
    
    private Stack<URL> history = new Stack();
    private Stack<URL> forwardHistory = new Stack();
    
    
    public WebBrowserModel() {}
    
    public WebBrowserModel(String location) {
        setLocation(location);
    }
    
    public void back() {
        if ( !history.isEmpty() ) {
            addForwardHistory( location );
            location = history.pop();
            refresh();
        }
    }
    
    public void forward() {
        if ( !forwardHistory.isEmpty() ) {
            addHistory( location );
            location = forwardHistory.pop();
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
        refresh();
    }
    
    public void clearCache() {
        history.clear();
        forwardHistory.clear();
        BrowserUtil.clearCache( getCacheContext() );
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void addHistory(URL location) {
        if ( location == null ) return;
        
        history.push(location);
        if ( history.size() > maxHistorySize) {
            history.setSize(maxHistorySize);
        }
    }
    
    private void addForwardHistory(URL location) {
        if ( location == null ) return;
        
        forwardHistory.push(location);
        if ( forwardHistory.size() > maxHistorySize) {
            forwardHistory.setSize(maxHistorySize);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  getters/setters  ">
    public String getCacheContext() {
        return "CACHE" + homeLocation.toExternalForm().hashCode();
    }
    
    public URL getLocation() {
        return location;
    }
    
    public void setRelativeLocation(String location) {
        try {
            setLocation(new URL( this.location, location ));
        } catch (MalformedURLException ex) {}
    }
    
    public void setLocation(String location) {
        try {
            if ( location == null ); //do nothing
            else if ( location.trim().startsWith("#") )
                setRelativeLocation( location );
            else if ( location.startsWith("www.") )
                setLocation(new URL("http://" + location));
            else
                setLocation(new URL(location));
        } catch(Exception e) {}
    }
    
    public void setLocation(URL location) {
        if ( location == null ) return;
        if ( !ValueUtil.isEqual(this.location, location) ) {
            if ( this.location != null )
                addHistory( this.location ); //push the old location
            
            this.location = location;
            
            if ( homeLocation == null ) {
                homeLocation = this.location;
            }
            refresh();
        }
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
