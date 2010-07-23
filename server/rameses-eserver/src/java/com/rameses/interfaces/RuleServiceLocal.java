/*
 * RuleServiceLocal.java
 *
 * Created on April 18, 2009, 9:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.interfaces;

import java.io.Reader;
import java.util.Map;

/**
 *
 * @author elmo
 */
public interface RuleServiceLocal {
    
    Object createFact(String name);
    Object createFact(String ruleName, String factName);
    void execute( String ruleName, Object[] facts );
    void execute( String ruleName, Object[] facts, String agenda );
    void execute( String ruleName, Object[] facts, String agenda, Map globals );
    void flushAll();
    void flush(String name);
    void redeploy( String name );
    void addPackage( String ruleBaseName, Reader rdr );
    void removePackage( String ruleBaseName, Reader rdr );
    
    
}
