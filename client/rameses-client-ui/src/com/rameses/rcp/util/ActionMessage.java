/*
 * ActionMessage.java
 *
 * Created on June 21, 2010, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.util;

import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jaycverg
 */
public class ActionMessage {
    
    private List<String> messages = new ArrayList();
    private Component source;
    
    public void addMessage(String code, String message, Object[] args) {
        message = MessageUtil.getMessage(code, message, "errors");
        
        if ( args != null ) {
            message = MessageFormat.format(message, args);
        }
        messages.add(message);
    }
    
    public void addMessage(ActionMessage message) {
        messages.addAll(message.messages);
        
        if ( source == null && message.source != null)
            source = message.source;
    }
    
    public void addMessage(ActionMessage message, String parentCaption) {
        boolean hasParentCaption = !ValueUtil.isEmpty(parentCaption);
        
        if( hasParentCaption ) messages.add(parentCaption + " (");        
        for (String msg : message.messages) {
            messages.add(msg);
        }        
        if( hasParentCaption ) messages.add(")");
        
        if ( source == null && message.source != null)
            source = message.source;
    }
    
    public boolean hasMessages() {
        return !messages.isEmpty();
    }
    
    public void clearMessages() {
        messages.clear();
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (String msg: messages) {
            if ( !first ) sb.append("\n");
            sb.append(msg);
            first = false;
        }
        return sb.toString();
    }
    
    public Component getSource() {
        return source;
    }
    
    public void setSource(Component source) {
        this.source = source;
    }
}
