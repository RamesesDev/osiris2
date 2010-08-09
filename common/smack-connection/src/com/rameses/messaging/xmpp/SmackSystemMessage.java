/*
 * SystemMessage.java
 *
 * Created on July 24, 2010, 10:12 AM
 * @author jaycverg
 */

package com.rameses.messaging.xmpp;

import com.rameses.messaging.*;


public class SmackSystemMessage extends SmackMessage implements SystemMessage {
    
    private Object requestId;
    private Object pushId;
    
    
    public SmackSystemMessage() {
       super();
    }
    
    public Object getRequestId() {
        return requestId;
    }
    
    public void setRequestId(Object reqId) {
        this.requestId = reqId;
    }
    
    public Object getPushId() {
        return pushId;
    }
    
    public void setPushId(Object pushId) {
        this.pushId = pushId;
    }
    
    
}
