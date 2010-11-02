package com.rameses.sql;

import com.sun.jmx.remote.util.Service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DefaultSqlUnitResourceProvider implements SqlUnitResourceProvider {
    
    private List<SqlUnitResourceProvider> providers;
    
    public DefaultSqlUnitResourceProvider() {
        //build the sql unit resource providers upon loading. not on demand!
        ClassLoader classLoader = getClass().getClassLoader();
        providers = new ArrayList();
        Iterator iter = Service.providers(SqlUnitResourceProvider.class, classLoader);
        while (iter.hasNext()) {
            SqlUnitResourceProvider sp = (SqlUnitResourceProvider)iter.next();
            providers.add(sp);
        }
    }
    
    public InputStream getResource(String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = null;
        for(SqlUnitResourceProvider sp : providers) {
            try {
                is = sp.getResource( name );
            } catch (Exception ign){;}
            if(is!=null) return is;
        }
        
        //if inputstream cannot be found by the providers, try classpath
        String fileName = "META-INF/sql/" + name;
        is = classLoader.getResourceAsStream(fileName);
        if (is==null)
            throw new RuntimeException("Sql resource " + name + " cannot be found!");
        return is;
    }
    
}