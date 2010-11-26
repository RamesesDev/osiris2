/*
 * ValidatorEvent.java
 *
 * Created on October 19, 2010, 2:56 PM
 * @author jaycverg
 */

package com.rameses.rcp.common;

import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.Map;


public class ValidatorEvent {
    
    private ActionMessage globalMessages = new ActionMessage();
    private Map<String, ActionMessage> messages = new LinkedHashMap();
    private Component source;
    private Binding binding;
    
    public ValidatorEvent(Binding binding) {
        this.binding = binding;
    }
    
    public void add(ValidatorEvent evt) {
        globalMessages.addMessage(evt.globalMessages);
        if ( evt.messages.size() > 0 )
            messages.putAll( evt.messages );
        
        if ( source == null && evt.source != null)
            source = evt.source;
    }
    
    public void addMessage(String entity, Object message) {
        if ( source == null && !ValueUtil.isEmpty(entity) ) 
            source = (Component) binding.find(entity);
        
        addMessage(entity, message, null);
    }
    
    public void addMessage(String entity, Object message, Object[] args) {
        if ( message == null ) return;
        
        if ( entity != null )
            getMessagesFor(entity).addMessage(null, message.toString(), args);
        else
            globalMessages.addMessage(null, message.toString(), args);
    }
    
    private ActionMessage getMessagesFor(String entity) {
        if ( messages.get(entity) == null )
            messages.put(entity, new ActionMessage());
        
        return messages.get(entity);
    }
    
    public Map<String, ActionMessage> getMessagesMap() {
        return messages;
    }
        
    public boolean hasMessages() {
        return !messages.isEmpty() || globalMessages.hasMessages();
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        
        if ( globalMessages.hasMessages() ) {
            sb.append( globalMessages.toString() );
        }
        
        boolean first = sb.length() == 0;
        for (ActionMessage msg: messages.values()) {
            if ( !first ) sb.append("\n");
            sb.append(msg.toString());
            first = false;
        }
        return sb.toString();
    }
    
    public Component getSource() {
        return source;
    }
    
}
