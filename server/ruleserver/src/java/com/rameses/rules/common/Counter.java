/*
 * Counter.java
 *
 * Created on September 15, 2010, 1:36 PM
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
public class Counter implements Serializable {
    
    private int value = 0;
    
    
    public Counter(int startNo) {
        value = startNo;
    }
    
    public void next() {
        value++;
    }

    public void decrease() {
        value--;
    }
    
    public int getValue() {
        return value;
    }
    
}
