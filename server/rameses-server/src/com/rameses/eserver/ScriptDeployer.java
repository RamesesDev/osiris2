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
    
    private List<String> deployers = new ArrayList();
    
    public void start() throws Exception {
        System.out.println("STARTING SCRIPT DEPLOYERS");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> e = loader.getResources("META-INF/deployers.conf");
        deployers.clear();
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

        String svc = null;
        String method = null; 
        for(String s : deployers) {
            try {
                if( s.indexOf(".")>0) {
                    svc = s.substring(0, s.indexOf("."));
                    method = s.substring( s.indexOf(".")+1 );
                }
                else {
                    svc = s;
                    method = "start";
                }
                ScriptServiceDelegate.getScriptService().invoke(svc,method,new Object[]{},new HashMap());
            }
            catch(Exception ex) {
                System.out.println("..... failed deployer for " + svc + "." + method + ". "  + ex.getMessage() );
            }
        }
    }

    public void stop() throws Exception {
        String svc = null;
        String method = null; 
        for(String s : deployers) {
            try {
                if( s.indexOf(".")<=0) {
                    svc = s;
                    method = "end";
                }
                ScriptServiceDelegate.getScriptService().invoke(svc,method,new Object[]{},new HashMap());
            }
            catch(Exception ex) {
                System.out.println("..... failed end deployer for " + svc + "." + method + ". "  + ex.getMessage() );
            }
        }
        System.out.println("STOPPING SCRIPT DEPLOYERS");
    }
    
}
