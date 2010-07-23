/*
 * DBUnit.java
 *
 * Created on March 27, 2010, 11:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.interfaces;

import java.util.Map;


public interface DBHandler {

    void execute( Map inputs,Map connections) throws Exception;
}
