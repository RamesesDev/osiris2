/*
 * DefaultSchemaResourceProvider.java
 *
 * Created on August 13, 2010, 10:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import com.sun.jmx.remote.util.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author elmo
 */
public class DefaultSchemaResourceProvider implements SchemaResourceProvider  {
    
    private List<SchemaResourceProvider> providers;
    
    public InputStream getResource(String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (providers==null) {
            providers = new ArrayList();
            Iterator iter = Service.providers(SchemaResourceProvider.class, classLoader);
            while (iter.hasNext()) {
                SchemaResourceProvider sp = (SchemaResourceProvider)iter.next();
                providers.add(sp);
            }
        }
        
        InputStream is = null;
        for(SchemaResourceProvider sp : providers) {
            try {
                is = sp.getResource( name );
            } catch (Exception ign){;}
            if(is!=null) return is;
        }
        
        //if inputstream cannot be found by the providers, try classpath
        String fileName = "META-INF/schema/" + name;
        is = classLoader.getResourceAsStream(fileName);
        if (is==null)
            throw new RuntimeException("Schema resource " + name + " cannot be found!");
        return is;
    }
}
