/*
 * ErrorManager.java
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


public final class ErrorManager {
    
    private static ErrorManager instance;
    
    public static ErrorManager getInstance() {
        if(instance==null) {
            instance = new ErrorManager();
        }
        return instance;
    }
    
    private List<ErrorHandler> errorHandlers;
    
    
    public boolean handleError(Exception e) {
        if(errorHandlers==null) {
            errorHandlers = new ArrayList();
            Iterator<ErrorHandler> iter = Service.providers(ErrorHandler.class, Thread.currentThread().getContextClassLoader());
            while(iter.hasNext()) {
                errorHandlers.add(iter.next());
            }
            Collections.sort(errorHandlers);
        }
        for(ErrorHandler eh: errorHandlers) {
            if( eh.accept(e) ) return true;
        }
        return false;
    }
    
    
}
