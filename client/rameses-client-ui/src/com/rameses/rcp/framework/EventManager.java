/*
 * EventManager.java
 *
 * Created on November 5, 2010, 1:36 PM
 */

package com.rameses.rcp.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jaycverg
 */
public final class EventManager {
    
    private Map<String, List<EventListener>> eventListeners = new HashMap();
    
    
    public EventManager() {}
    
    public void notify(String name, ControlEvent evt) {
        List<EventListener> listeners = getListenersFor(name, false);
        if ( listeners == null ) return;
        
        for(EventListener l : listeners ) {
            l.onEvent( evt );
        }
    }
    
    public List getListenersFor(String name, boolean forceCreate) {
        if ( !eventListeners.containsKey(name) ) {
            if ( !forceCreate ) return null;
            eventListeners.put(name, new ArrayList());
        }
        
        return eventListeners.get(name);
    }
    
    public void addListener(String name, EventListener listener) {
        List listeners = getListenersFor(name, true);
        if ( !listeners.contains(listener) )
            listeners.add(listener);
    }
    
    public void removeListener(String name, EventListener listener) {
        List listeners = getListenersFor(name, false);
        if ( listeners != null && listeners.contains(listener) )
            listeners.remove(listener);
    }
    
}
