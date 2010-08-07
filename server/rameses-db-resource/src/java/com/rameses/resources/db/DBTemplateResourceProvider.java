/*
 * DBScriptProvider.java
 *
 * Created on December 6, 2009, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.resources.db;

import com.rameses.eserver.ResourceProvider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.naming.InitialContext;


public class DBTemplateResourceProvider extends ResourceProvider {
    
    
    public String getName() {
        return "template" ;
    }
    
    public String getDescription() {
        return "DB Template Resource Provider [template//]";
    }
    
    
    public boolean accept(String nameSpace) {
        return (nameSpace.equalsIgnoreCase("template"));
    }

    public int getPriority() {
        return 100;
    }
    
    public InputStream getResource(String name) throws Exception {
        try {
            InitialContext ctx = new InitialContext();
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
            byte[] b = ds.getTemplateResource(name);
            if( b!= null )
                return new ByteArrayInputStream(b);
            else
                return null;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
   
    
}
