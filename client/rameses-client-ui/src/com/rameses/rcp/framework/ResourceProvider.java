/*
 * ResourceProvider.java
 *
 * Created on November 19, 2009, 1:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.framework;

import java.io.InputStream;

/**
 *
 * @author elmo
 */
public interface ResourceProvider {
    InputStream getResource(String name);
}
