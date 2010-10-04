/*
 * AppException.java
 *
 * Created on October 4, 2010, 2:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

/**
 *
 * @author ms
 */
public class AppException extends RuntimeException {
    public static long serialVersionUID = 1L;
    public AppException() {
    }
    
    public AppException(String msg) {
        super(msg);
    }
    
    
}
