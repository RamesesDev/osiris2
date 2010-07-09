/*
 * ResourceProviderImpl.java
 *
 * Created on November 19, 2009, 1:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.impl;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ResourceProvider;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author elmo
 */
public class ResourceProviderImpl implements ResourceProvider {
    
    public InputStream getResource(String name) {
        if( name.contains("://")) {
            try {
                return new URL(name).openStream();
            }
            catch(Exception ign) {
                return null;
            }
        }
        else {
            ClassLoader loader = ClientContext.getCurrentContext().getClassLoader();
            return loader.getResourceAsStream(name);
        }
    }
    
}
