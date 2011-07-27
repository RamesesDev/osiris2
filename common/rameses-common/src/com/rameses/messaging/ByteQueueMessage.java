/*
 * ByteQueueMessage.java
 * Created on July 23, 2011, 10:18 AM
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
public class ByteQueueMessage implements QueueMessage {
    
    private byte[] bytes;
    
    public ByteQueueMessage(byte[] bytes) {
        this.bytes = bytes;
    }

    public Object getMessage() {
        return this.bytes;
    }
    
}
