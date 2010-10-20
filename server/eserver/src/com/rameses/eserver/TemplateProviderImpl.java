/*
 * TemplateProviderImpl.java
 *
 * Created on October 18, 2010, 4:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.util.TemplateProvider;
import java.io.InputStream;

/**
 *
 * @author ms
 */
public class TemplateProviderImpl extends  TemplateProvider.DefaultTemplateProvider {
    
    public TemplateProviderImpl() {
        super();
    }

    public InputStream getResourceStream(String name) {
        System.out.println("getting resource " + name + "from db");
        return super.getResourceStream( name );
    }
    
}
