/*
 * TemplateResourceProvider.java
 *
 * Created on July 17, 2010, 10:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.templates;

import com.rameses.eserver.AbstractResourceProvider;
import java.io.InputStream;

/**
 * Users must use the following URL pattern
 *
 * e.g.
 *   templates://myfile/myrsource.groovy
 *
 *  where my file corresponds to the folder under META-INF/templates
 */
public class TemplateResourceProvider extends AbstractResourceProvider {
    
    public TemplateResourceProvider() {
    }

    public String getName() {
        return "template";
    }

    public String getDescription() {
        return "Template Resource Provider [template://]";
    }

    public int getPriority() {
        return 0;
    }

    public boolean accept(String nameSpace) {
        return ( nameSpace.equals("template"));
    }

    public InputStream getResource(String name) throws Exception{
        String fileName = "META-INF/templates/"+name;
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    
}
