/*
 * ResponseServiceLocal.java
 *
 * Created on July 5, 2010, 12:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.interfaces;

/**
 *
 * @author ms
 */
public interface AsyncResponseServiceLocal {
    
    void pushResponse( String requestId, String requester, Object data );
    Object getPollData(String requestId);
    Object getPushData(String id);
    
    
}
