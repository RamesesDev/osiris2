/*
 * ServiceHandler.java
 *
 * Created on June 24, 2012, 11:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.Map;

/**
 *
 * @author Elmo
 */
public interface ServiceAdapter {
    Object create(String name, Map conf);
    String getName();
    Map getClassInfo(String name, Map conf);
}
