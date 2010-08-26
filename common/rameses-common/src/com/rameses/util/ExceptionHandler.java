/*
 * ExceptionHandler.java
 *
 * Created on August 24, 2010, 4:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

/**
 *
 * @author elmo
 */
public abstract class ExceptionHandler implements Comparable {
    
    public abstract boolean accept(Exception e);
    
    public int getIndex() {
        return 0;
    }

    public int compareTo(Object o) {
        ExceptionHandler eh = (ExceptionHandler)o;
        if(getIndex()>eh.getIndex()) return 1;
        else if(getIndex()<eh.getIndex()) return -1;
        return 0;
    }
    
}
