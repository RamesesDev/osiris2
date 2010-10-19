/*
 * ValidatorEvent.java
 *
 * Created on October 19, 2010, 2:56 PM
 * @author jaycverg
 */

package com.rameses.rcp.common;

import com.rameses.rcp.util.ActionMessage;
import java.util.LinkedHashMap;
import java.util.Map;


public class ValidatorEvent {
    
    private Map<String, ActionMessage> messages = new LinkedHashMap();
    
    
    public ValidatorEvent() {}
    
    public void addMessage(String entity, Object message) {
        addMessage(entity, message, null);
    }
    
    public void addMessage(String entity, Object message, Object[] args) {
        if ( message != null )
            getMessagesFor(entity).addMessage(null, message.toString(), args);
    }
    
    private ActionMessage getMessagesFor(String entity) {
        if ( messages.get(entity) == null )
            messages.put(entity, new ActionMessage());
        
        return messages.get(entity);
    }
    
    public Map getMessagesMap() {
        return messages;
    }
    
}
