/*
 * ResourceHandler.java
 *
 * Created on July 19, 2010, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.interfaces;

import java.io.InputStream;

/**
 *
 * @author elmo
 */
public interface ResourceHandler {

    void handle( InputStream is, String resName ) throws Exception ;
    
}
