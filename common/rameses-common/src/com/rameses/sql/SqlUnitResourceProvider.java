/*
 * SqlResourceProvider.java
 *
 * Created on August 13, 2010, 5:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.InputStream;

/**
 *
 * To plugin different sql resources, register this in META-INF/services
 */
public interface SqlUnitResourceProvider {
    
    InputStream getResource(String name);
    
}
