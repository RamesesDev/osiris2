/*
 * EventQueueFactory.java
 * Created on July 18, 2011, 1:19 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.concurrent;

import com.rameses.messaging.bak.EventQueueProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class EventQueueFactory {
    
    private Map<String, EventQueueProvider> entries = Collections.synchronizedMap(new HashMap());
    
    public EventQueueFactory() {
    }
    
    public EventQueueProvider getEventQueue(String name, EventHandler handler ) {
        if(!entries.containsKey(name)) {
            EventQueueProvider queue = new EventQueueProvider(name, handler);
            entries.put(name, queue);
        }
        return entries.get(name);
    }
    
}
