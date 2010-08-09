/*
 * Message.java
 *
 * Created on July 24, 2010, 10:07 AM
 * @author jaycverg
 */

package com.rameses.messaging;


public interface Message {
    
    Object getMessage();
    void setMessage(Object message);
    void setRecipient(Object recipient);
    void setSender(Object sender);
    
}
