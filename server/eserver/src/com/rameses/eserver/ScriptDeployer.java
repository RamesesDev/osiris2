/*
 * ScriptDeployer.java
 *
 * Created on October 23, 2010, 8:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.io.LineReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ms
 */
public class ScriptDeployer implements ScriptDeployerMBean {
    
    public void start() throws Exception {
        System.out.println("      Initiating Script Deployers");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> e = loader.getResources("META-INF/deployers.conf");
        final List<String> deployers = new ArrayList();
        while(e.hasMoreElements()) {
            URL u = e.nextElement();
            InputStream is = null;
            try {
                is = u.openStream();
                LineReader line = new LineReader();
                line.read( is, new LineReader.Handler(){
                    public void read( String s) {
                        if(deployers.indexOf(s)<0) deployers.add( s );
                    }
                });
            } catch(Exception ex) {
                System.out.println("Error reading " + u);
            } finally {
                try {is.close();}catch(Exception ign){;}
            }
        }
        for(String s : deployers) {
            String svc = null;
            String method = null; 
            try {
                svc = s.substring(0, s.indexOf("."));
                method = s.substring( s.indexOf(".")+1 );
                ScriptServiceDelegate.getScriptService().invoke(svc,method,new Object[]{},new HashMap());
            }
            catch(Exception ex) {
                System.out.println("..... failed deployer for " + svc + "." + method + ". "  + ex.getMessage() );
            }
        }
    }

    public void stop() throws Exception {
        System.out.println("      Stopping Script Deployers");
    }
    
}
