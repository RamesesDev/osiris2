/*
 * DsLoaderResourceProvider.java
 *
 * Created on August 7, 2010, 2:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.resources.db;

import com.rameses.eserver.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import javax.naming.InitialContext;

/**
 *
 * @author elmo
 */
public class DsLoaderResourceProvider extends ResourceProvider {
    
    public DsLoaderResourceProvider() {
    }
    
    public String getName() {
        return "ds-loader";
    }
    
    public String getDescription() {
        return "Ds Loader Provider [ds-loader://]";
    }
    
    public int getPriority() {
        return 0;
    }
    
    public boolean accept(String nameSpace) {
        return nameSpace.equals("ds-loader");
    }
    
    public InputStream getResource(String name) throws Exception {
        try {
            InitialContext ctx = new InitialContext();
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");
            byte[] b = ds.getDsResource(name);
            if( b!= null )
                return new ByteArrayInputStream(b);
            else
                return null;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void scanResources(String name, MultiResourceHandler handler) throws Exception {
         try {
            InitialContext ctx = new InitialContext();
            InputStream is = null;             
            DBResourceServiceLocal ds = (DBResourceServiceLocal)ctx.lookup(DBResourceService.class.getSimpleName()+"/local");

            List<String> list = ds.getDsList();
            
            for(String n : list ) {
                try {
                    is = getResource(n);
                    handler.handle( is, n );
                }
                catch(Exception es) {
                    System.out.println("ERROR LOADING " + es.getMessage());
                } 
                finally {
                    try { is.close(); } catch(Exception ign){;}
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
         
    }
    
    
}
