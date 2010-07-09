/*
 * MethodResolver.java
 *
 * Created on August 27, 2009, 8:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

/**
 *
 * @author elmo
 */
public interface MethodResolver {
    
    Object invoke( Object bean, String action, Class[] paramTypes, Object[] args ) throws Exception;
    
    
}
