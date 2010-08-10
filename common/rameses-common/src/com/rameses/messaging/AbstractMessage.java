/*
 * AbstractMessage.java
 *
 * Created on August 10, 2010, 8:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.messaging;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmo
 */
public abstract class AbstractMessage implements Message {
    
    private String channel;
    private String sender;
    private List<String> receivers = new ArrayList();
    private String subject;
    
    public abstract String getType();
    public abstract String getBody();
    
    public void addReceiver(String r) {
        receivers.add(r);
    }

    public String[] getReceivers() {
        return receivers.toArray(new String[]{});
    }
    
    public String getSender() {
        return sender;
    }
    
    public void setSender(String s) {
        this.sender = s;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    
}
