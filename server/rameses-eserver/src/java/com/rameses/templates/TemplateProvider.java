/*
 * TemplateProvider.java
 *
 * Created on July 17, 2010, 9:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.templates;

import java.io.InputStream;

/**
 * Providers are used to serve the template
 */
public interface TemplateProvider {
     
    boolean accept(String fileName);
    Template createTemplate(InputStream is) throws Exception; 
    
    
}
