/*
 * AggregatorHandler.java
 *
 * Created on September 3, 2010, 8:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import java.util.Map;

/**
 *
 * @author elmo
 */
public interface AggregateHandler {
    
    /**
     * returns true if we can proceed with the replace or update.
     * compare old model and newModel to see if we should proceed.
     */
    boolean proceed(Object oldModel, Object newModel);
    
    /**
     * return the correct value that will be used for the update. 
     */    
    Object compare( String aggregatorType, Class returnClass, Object oldData, Object newData, Map options );
    
}
