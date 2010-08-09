/*
 * MessagingConnection.java
 *
 * Created on July 24, 2010, 9:37 AM
 * @author jaycverg
 */

package com.rameses.messaging;

import java.util.Map;

public abstract class SystemConnection extends MessagingConnection {
    
    public final void sendMessage(Message message) {}
    public final Message createMessage(Map properties) { return null; }
    
    public abstract void sendMessage(SystemMessage message);
    public abstract SystemMessage createSystemMessage(Map properties);
    
}
