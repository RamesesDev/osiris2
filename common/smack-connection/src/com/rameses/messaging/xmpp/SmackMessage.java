/*
 * SystemMessage.java
 *
 * Created on July 24, 2010, 10:12 AM
 * @author jaycverg
 */

package com.rameses.messaging.xmpp;

import com.rameses.messaging.*;


public class SmackMessage extends Message {
    
    private org.jivesoftware.smack.packet.Message smackMessage;
    
    public SmackMessage() {
        smackMessage = new org.jivesoftware.smack.packet.Message();
    }
    
    public Object getMessage() {
        return smackMessage;
    }
    
    public void setMessage(Object message) {
        if ( message instanceof org.jivesoftware.smack.packet.Message ) {
            this.smackMessage = new org.jivesoftware.smack.packet.Message();
        }
        else if ( message instanceof String ) {
            this.smackMessage.setBody( message.toString() );
        }
    }
    
    public void setRecipient(Object name) {
        smackMessage.setTo( name+"" );
    }

    public void setSender(Object sender) {
        smackMessage.setFrom( sender+"" );
    }
    
    
}
