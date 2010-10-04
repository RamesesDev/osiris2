/*
 * AggregatorHandler.java
 *
 * Created on September 3, 2010, 8:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

/**
 *
 * @author elmo
 */
public interface AggregateCollector {
    
    boolean accept(Object oldModel, Object newModel);
  
}
