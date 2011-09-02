/*
 * TemplateProviderLoader.java
 * Created on April 20, 2011, 10:57 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver.loaders;

import com.rameses.eserver.ServiceLoader;
import com.rameses.eserver.TemplateProviderImpl;
import com.rameses.util.TemplateProvider;

/**
 *
 * @author jzamss
 */
public class TemplateProviderLoader implements ServiceLoader {
    
    public int getIndex() {
        return 0;
    }

    public void load() throws Exception {
        System.out.println("      Initializing Template Manager");
        TemplateProvider.setInstance(new TemplateProviderImpl());
    }

    public void unload() throws Exception {
        System.out.println("      Unloading Template Provider");
        TemplateProvider.setInstance( null );
    }
    
}
