/*
 * ConfMgmtMBean.java
 *
 * Created on August 5, 2010, 8:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import java.util.Map;

/**
 *
 * @author elmo
 */
public interface ConfMgmtMBean {
    void start() throws Exception;
    void reload() throws Exception;
    void stop() throws Exception;
    Object getProperty(Object key);
    Object getProperty(Object key, Object defaultValue);
    Map getVars();
}
