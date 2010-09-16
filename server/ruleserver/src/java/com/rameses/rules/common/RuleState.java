/*
 * State.java
 *
 * Created on September 15, 2010, 1:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules.common;

import java.io.Serializable;

/**
 *
 * @author elmo
 */
public class RuleState implements Serializable {
    
    private String name;
    
    public RuleState(String name) {
       this.name = name; 
    }

    public String getName() {
        return name;
    }
    
}
