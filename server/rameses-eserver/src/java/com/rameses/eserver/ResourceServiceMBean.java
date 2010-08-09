/*
 * ResourceServiceLocal.java
 *
 * Created on July 15, 2010, 9:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.interfaces.*;
import java.io.InputStream;

/**
 * Only input streams are allowed for resources
 * Name passed must be similar to URL
 * i.e. it follows pattern http://resource.file
 * Why are we not using URLStreamHandler?
 * Because we want this to be pluggable layer.
 * i.e. we can move remove resource providers on the fly.
 */
public interface ResourceServiceMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    InputStream getResource(String name) throws Exception;
    void scanResources( String name, ResourceHandler handler ) throws Exception;
    
    
}
