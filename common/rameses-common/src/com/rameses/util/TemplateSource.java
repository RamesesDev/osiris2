/*
 * TemplateSource.java
 *
 * Created on April 22, 2012, 4:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.io.InputStream;

/**
 *
 * @author Elmo
 */
public interface TemplateSource {
    InputStream getSource(String name);
}
