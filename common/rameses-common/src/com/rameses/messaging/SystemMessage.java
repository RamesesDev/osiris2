/*
 * Message.java
 *
 * Created on July 24, 2010, 10:07 AM
 * @author jaycverg
 */

package com.rameses.messaging;


public interface SystemMessage extends Message {

    public Object getRequestId();
    public void setRequestId(Object reqId);
    public Object getPushId();
    public void setPushId(Object pushId);
    
}
