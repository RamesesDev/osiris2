/*
 * SchemaResourceProvider.java
 *
 * Created on August 13, 2010, 10:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.io.InputStream;

/**
 *
 * @author elmo
 */
public interface SchemaResourceProvider {
    InputStream getResource(String name);
}
