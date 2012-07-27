/*
 * UserPrincipal.java
 *
 * Created on July 23, 2012, 9:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.HashMap;

/**
 *
 * @author Elmo
 */
public abstract class UserPrincipal extends HashMap {
    
    public String getName() {
        return (String)get("name");
    }
    
}
