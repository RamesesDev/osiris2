/*
 * StringQueueMessage.java
 * Created on July 23, 2011, 12:20 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.messaging;

/**
 *
 * @author jzamss
 */
public class TextQueueMessage implements QueueMessage {
    
    private String value;
    
    public TextQueueMessage(String s) {
        this.value = s;
    }

    public Object getMessage() {
        return value;
    }
    
}
