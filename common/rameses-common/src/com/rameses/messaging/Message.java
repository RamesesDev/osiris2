/*
 * Message.java
 *
 * Created on July 24, 2010, 10:07 AM
 * @author jaycverg
 */

package com.rameses.messaging;


public interface Message {
    
    /**
     * refers to what channel or thread this will be sent.
     */
    String getChannel();
    
    /**
     * string representation of the type of message
     */
    String getType();
    
    /**
     * subject of the message
     */
    String getSubject();
    
    /**
     * list of reeceivers
     */
    String[] getReceivers();
    
    /**
     * string content
     */
    String getBody();
    
    /**
     * origin or sender of message
     */
    String getSender();
    
}
