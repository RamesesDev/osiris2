/*
 * AsyncEvent.java
 *
 * Created on July 18, 2010, 9:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.io.Serializable;

/**
 * @author elmo
 */
public class AsyncEvent implements Serializable {
    
    private int loop;
    
    /** Creates a new instance of AsyncEvent */
    public AsyncEvent() {
    }
    
    public void moveNext() {
        loop++;
    }

    public int getLoop() {
        return loop;
    }
    
}
