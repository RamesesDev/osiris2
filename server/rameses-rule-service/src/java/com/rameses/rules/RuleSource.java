/*
 * RuleSource.java
 *
 * Created on July 2, 2009, 9:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import java.io.Reader;
import java.io.Serializable;

/**
 *
 * @author elmo
 */
public class RuleSource implements Serializable {
    
    private String name;
    private Reader source;
    
    public RuleSource(String name, Reader rdr) {
        this.name = name;
        this.source = rdr;
    }

    public String getName() {
        return name;
    }

    public Reader getSource() {
        return source;
    }
    
}
