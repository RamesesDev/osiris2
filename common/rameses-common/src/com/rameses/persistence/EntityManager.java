/*
 * EntityManager.java
 *
 * Created on August 18, 2010, 8:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

/**
 *
 * @author elmo
 */
public interface EntityManager {
    
    Object create(String schemaName, Object data);
    Object read(String schemaName, Object data);
    Object update(String schemaName, Object data);
    void delete(String schemaName, Object data);
    
}
