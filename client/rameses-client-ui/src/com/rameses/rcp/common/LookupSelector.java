/*
 * ItemSelection.java
 *
 * Created on August 28, 2009, 10:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.common;

/**
 *
 * @author elmo
 */
public interface LookupSelector {
    void select(Object o);
    void cancelSelection();
}
