/*
 * ActionInvokerListener.java
 *
 * Created on September 15, 2010, 12:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules.common;

/**
 *
 * @author elmo
 */
public interface RuleActionHandler {
    
    void execute(Object context, Object params);
    
}
