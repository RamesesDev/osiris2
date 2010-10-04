/*
 * ExceptionManager.java
 *
 * Created on August 24, 2010, 4:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import com.sun.jmx.remote.util.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public final class ExceptionManager {
    
    private static ExceptionManager instance;
    
    public static ExceptionManager getInstance() {
        if(instance==null) {
            instance = new ExceptionManager();
        }
        return instance;
    }
    
    private List<ExceptionHandler> errorHandlers;
    
    
    public boolean handleError(Exception e) {
        if(errorHandlers==null) {
            errorHandlers = new ArrayList();
            Iterator<ExceptionHandler> iter = Service.providers(ExceptionHandler.class, Thread.currentThread().getContextClassLoader());
            while(iter.hasNext()) {
                errorHandlers.add(iter.next());
            }
            Collections.sort(errorHandlers);
        }

        for(ExceptionHandler eh: errorHandlers) {
            if( eh.accept(e) ) return true;
        }
        return false;
    }
    
    public static Exception getOriginal(Exception ex) {
        Throwable t = ex;
        while( t.getCause() != null) {
            t = t.getCause();
        }
        Exception e = null;
        if( t instanceof AppException) {
            e = (AppException)t;
        }
        if( t instanceof NullPointerException ) {
            e = (NullPointerException)t;
        }
        else {
            String msg = t.getMessage();
            if( msg == null ) {
                if( msg == null )  msg = t.getClass().getName(); 
            }
            e = new Exception(msg);
        }
        return e;
    }
    
    
}
