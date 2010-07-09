package com.rameses.deployer;

import com.rameses.interfaces.ResourceProvider;
import com.rameses.interfaces.ScriptServiceLocal;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import javax.naming.InitialContext;

public class DeployerService implements DeployerServiceMBean, Serializable {
    
    private ScriptServiceLocal scriptService;
    
    public void start() {
        //load the deployers
        System.out.println("START DEPLOYERS");
        
        //initialize scriptService immediately
        Iterator iter = com.sun.jmx.remote.util.Service.providers(ResourceProvider.class, Thread.currentThread().getContextClassLoader());
        //load only the first
        ResourceProvider scriptProvider = null;
        while(iter.hasNext()) {
            ResourceProvider rp = (ResourceProvider)iter.next();
            if(rp.getNamespace().equals(ResourceProvider.CONF)) {
                scriptProvider = rp;
                break;
            }
        }
        
        try {
            InitialContext ctx = new InitialContext();
            scriptService = (ScriptServiceLocal)ctx.lookup("ScriptService/local");
            
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if( scriptProvider!=null ) {
                try {
                    InputStream is = scriptProvider.getResource("deployers");
                    if(is!=null) loadDeployers( is );
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            Enumeration en = loader.getResources("META-INF/deployers.conf");
            while(en.hasMoreElements()) {
                URL u = (URL)en.nextElement();
                loadDeployers( u.openStream() );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadDeployers(InputStream is ) throws Exception {
        InputStreamReader ir = new InputStreamReader(is);
        BufferedReader r = new BufferedReader(ir);
        String s = null;
        while((s=r.readLine())!=null) {
            if( s.trim().length()==0 || s.trim().startsWith("#"))
                continue;
            
            try {
                String svcName = s;
                String method = "start";
                if( s.indexOf(".")>0 ) {
                    svcName = s.substring(0, s.indexOf("."));
                    method = s.substring(s.indexOf(".")+1);
                }
                scriptService.invoke(svcName,method, new Object[]{}, null);
            } catch(Exception ign) {
                System.out.println(ign.getMessage());
            }
        }
    }
    
    public void stop() {
        System.out.println("STOPPING DEPLOYERS");
    }
    
    
}
