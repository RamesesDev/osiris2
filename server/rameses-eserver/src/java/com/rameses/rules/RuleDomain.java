/*
 * RuleDomain.java
 *
 * Created on August 1, 2010, 6:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public interface RuleDomain {
    
    String getName();
    void load() throws Exception;
    Object createObject(String name);
    void execute(List facts, Map globals, String agenda );
    
}
