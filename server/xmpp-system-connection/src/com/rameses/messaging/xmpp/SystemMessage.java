/*
 * SystemMessage.java
 *
 * Created on July 24, 2010, 10:12 AM
 * @author jaycverg
 */

package com.rameses.messaging.xmpp;

import com.rameses.messaging.*;


public class SystemMessage extends Message {
    
    private org.jivesoftware.smack.packet.Message smackMessage;
    
    public SystemMessage() {
        smackMessage = new org.jivesoftware.smack.packet.Message();
    }

    public Object getMessage() {
        return smackMessage;
    }
    
    public void setText(String text) {
        smackMessage.setBody(text);   
    }
    
    public void setRecipient(String name) {
        smackMessage.setTo(name);
    }
    
}
