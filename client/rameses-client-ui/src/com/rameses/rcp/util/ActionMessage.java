/*
 * ActionMessage.java
 *
 * Created on June 21, 2010, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.util;

import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author jaycverg
 */
public class ActionMessage {
    
    private Set<String> messages = new LinkedHashSet();
    
    public void addMessage(String code, String message, Object[] args) {
        message = MessageUtil.getMessage(code, message, "errors");
        
        if ( args != null ) {
            message = MessageFormat.format(message, args);
        }
        messages.add(message);
    }
    
    public void addMessage(ActionMessage message) {
        messages.addAll(message.messages);
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
}
