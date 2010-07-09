/*
 * InvokerFilter.java
 *
 * Created on December 8, 2009, 9:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;

/**
 *
 * @author elmo
 */
public interface InvokerFilter {
    boolean accept(Invoker o);
}
