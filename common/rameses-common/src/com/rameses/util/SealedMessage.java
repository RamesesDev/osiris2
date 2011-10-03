/*
 * SealedMessage.java
 * Created on September 19, 2011, 4:40 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.util;

import java.io.Serializable;

/**
 *
 * @author jzamss
 */
public class SealedMessage implements Serializable {
    
    private Object object;
    private String enctype = "AES";
    
    /** Creates a new instance of SealedMessage */
    public SealedMessage(Object o) {
        this(o,"AES");
    }
    public SealedMessage(Object o, String enctype) {
        try {
            this.enctype = enctype;
            object = CipherUtil.encode((Serializable)o,enctype);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Object getMessage() {
        try {
            return CipherUtil.decode((Serializable)object, enctype);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
