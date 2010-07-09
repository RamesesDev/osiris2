/*
 * DBScriptProvider.java
 *
 * Created on December 6, 2009, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting.db;

import com.rameses.interfaces.ResourceProvider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.naming.InitialContext;


public class DBScriptProvider implements ResourceProvider {
    
    public String getNamespace() {
        return ResourceProvider.SERVICE;
    }
    
    public InputStream getResource(String name) {
        try {
            InitialContext ctx = new InitialContext();
            DBScriptProviderServiceLocal ds = (DBScriptProviderServiceLocal)ctx.lookup(DBScriptProviderService.class.getSimpleName()+"/local");
            byte[] b = ds.getInfo(name,DBScriptInfo.class.getName() );
            if( b!= null )
                return new ByteArrayInputStream(b);
            else
                return null;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    
    
}
