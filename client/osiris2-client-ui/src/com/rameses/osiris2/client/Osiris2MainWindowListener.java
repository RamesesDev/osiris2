/*
 * Osiris2MainWindowListener.java
 *
 * Created on August 27, 2010, 11:21 AM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.platform.interfaces.MainWindowListener;
import com.rameses.rcp.framework.ClientContext;
import java.util.ArrayList;
import java.util.List;


public class Osiris2MainWindowListener implements MainWindowListener {
    
    public List<MainWindowListener> listeners = new ArrayList();
    
    public Osiris2MainWindowListener() {
    }
    
    public Object onEvent(String eventName, Object evt) {
        for(MainWindowListener l: listeners) {
            l.onEvent(eventName, evt);
        }
        return null;
    }
    
    public boolean onClose() {
        for(MainWindowListener l: listeners) {
            try {
                if( !l.onClose() ) return false;
                
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        //stop taskmanager only if all listeners allow the platform to be closed
        try {
            ClientContext.getCurrentContext().getTaskManager().stop();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    public void add(MainWindowListener listener) {
        if ( !listeners.contains(listener) ) {
            listeners.add(listener);
        }
    }
    
    public void remove(MainWindowListener listener) {
        listeners.remove(listener);
    }
    
    public void clear() {
        listeners.clear();
    }
    
}
