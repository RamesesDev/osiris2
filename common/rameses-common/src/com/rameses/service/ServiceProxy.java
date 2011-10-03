/*
 * EJBServiceProxy.java
 *
 * Created on September 23, 2011, 12:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.service;

/**
 *
 * @author jzamss
 */
public interface ServiceProxy {
    public Object invoke(String action, Object[] params) throws Exception;
    public Object invoke(String action) throws Exception;
}
