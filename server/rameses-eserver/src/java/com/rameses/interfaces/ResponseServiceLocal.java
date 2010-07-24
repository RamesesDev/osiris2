/*
 * ResponseServiceLocal.java
 *
 * Created on July 5, 2010, 12:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.interfaces;

import java.util.List;

/**
 *
 * @author ms
 */
public interface ResponseServiceLocal {
    
    void registerData( String requestId, Object data );
    void removeStaleObjects();
    Object getResponseData( String requestId );
    
}
