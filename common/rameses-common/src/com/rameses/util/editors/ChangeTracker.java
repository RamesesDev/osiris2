/*
 * ChangeTracker.java
 *
 * Created on August 23, 2010, 4:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util.editors;

/**
 *
 * returns key-value pair of changes.
 */
public class ChangeTracker {
    
    /***
     * sample output
     * object: order
     *
     * updates:
     * orderno = 112678
     * customer  = ABC
     * items[key=ABC001]/qty = 20 
     * items[key=ABC001]/price = 120.00
     * items[key=ABC001]/total = 240.00
     *
     * adds: 
     * items = [key:"OBJAISIAIS", first: "first"] //should add all and items below.
     *
     * deletes:
     * items = [key:"OBJAISIAIS", first: "first"] //should delete all items below.
     */
    
    public ChangeTracker() {
    }
    
}
