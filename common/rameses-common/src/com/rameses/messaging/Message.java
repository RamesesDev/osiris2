/*
 * Message.java
 *
 * Created on July 24, 2010, 10:07 AM
 * @author jaycverg
 */

package com.rameses.messaging;


public abstract class Message {
    
    public abstract Object getMessage();
    public abstract void setMessage(Object message);
    public abstract void setRecipient(Object recipient);
    public abstract void setSender(Object sender);
    
}
