/*
 * SchemaManagerLoader.java
 * Created on April 20, 2011, 10:59 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver.loaders;

import com.rameses.eserver.SchemaManagerImpl;
import com.rameses.eserver.ServiceLoader;
import com.rameses.schema.SchemaManager;


public class SchemaManagerLoader implements ServiceLoader {
    
    public int getIndex() {
        return 1;
    }
    
    public void load() throws Exception {
        System.out.println("      Initializing Schema Manager");
        SchemaManager.setInstance( new SchemaManagerImpl() );
    }
    
    public void unload() throws Exception {
        System.out.println("      Unloading Schema Manager");
        SchemaManager.setInstance( null );
    }
    
}
