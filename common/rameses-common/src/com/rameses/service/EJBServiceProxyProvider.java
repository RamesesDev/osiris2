/*
 * ServiceProxyProvider.java
 *
 * Created on September 21, 2011, 9:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.service;

import java.util.Map;


public interface EJBServiceProxyProvider {
    boolean accept( Map map);
    ServiceProxy create(String name, Map conf);
}
