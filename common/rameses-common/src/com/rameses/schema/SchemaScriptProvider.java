/*
 * SchemaScriptProvider.java
 *
 * Created on August 28, 2010, 8:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

/**
 *
 * @author elmo
 */
public interface SchemaScriptProvider {
    
    Object eval(Object vars, String data);
    
}
