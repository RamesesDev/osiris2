package com.rameses.deployer;

import com.rameses.interfaces.ScriptServiceLocal;
import com.rameses.resource.MultiResourceHandler;
import com.rameses.resource.ResourceServiceMBean;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.naming.InitialContext;

public class DeployerService implements DeployerServiceMBean {
    
    private final static String SCRIPT_PREFIX = "script";
    private final static String DEPLOYERS = "deployers";
    private ScriptServiceLocal scriptService;
    private ResourceServiceMBean resourceService;
    
    public void start() throws Exception {
        System.out.println("START SCRIPT DEPLOYERS");
        InitialContext ctx = new InitialContext();
        scriptService = (ScriptServiceLocal)ctx.lookup("ScriptService/local");
        resourceService = (ResourceServiceMBean)ctx.lookup("ResourceService");
        resourceService.scanResources(SCRIPT_PREFIX+"://"+DEPLOYERS, new DeployerHandler());
    }
    
    public void stop() {
        System.out.println("STOPPING SCRIPT DEPLOYERS");
    }
    
    private class DeployerHandler implements MultiResourceHandler {
        
        public void handle(InputStream is) throws Exception {
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
    }
    
    
    
}
