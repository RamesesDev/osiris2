/*
 * ResponseService.java
 *
 * Created on June 13, 2012, 9:50 AM
 * @author jaycverg
 */

package com.rameses.xmpp.service;

import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;


public class ResponseService implements ResponseServiceMBean {
    
    private String remoteHost;
    private String remoteContext;
    private String remoteServiceName = "ResponseService";
    
    public ResponseService() {
    }
    
    public void start() throws Exception {
        System.out.println("BINDING RESPONSE SERVICE");
        InitialContext ictx = new InitialContext();
        JndiUtil.bind( ictx, AppContext.getPath() + ResponseService.class.getSimpleName(), this );
    }
    
    public void stop() throws Exception {
        System.out.println("UNBINDING RESPONSE SERVICE");
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind(ictx, AppContext.getPath() + ResponseService.class.getSimpleName());
    }
    
    public void sendResponse(Map request, Object message) throws Exception {
        Map conf = new HashMap();
        conf.put("app.host", remoteHost);
        conf.put("app.context", remoteContext);
        ScriptServiceContext ssc = new ScriptServiceContext(conf);
        ServiceProxy proxy = ssc.create(remoteServiceName);
        proxy.invoke("sendResponse", new Object[]{ request, message});
    }
    
    public Object getResponse(String requestId) throws Exception {
        Map conf = new HashMap();
        conf.put("app.host", remoteHost);
        conf.put("app.context", remoteContext);
        ScriptServiceContext ssc = new ScriptServiceContext(conf);
        ServiceProxy proxy = ssc.create(remoteServiceName);
        return proxy.invoke("getResponse", new Object[]{requestId});
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public String getRemoteHost() {
        return remoteHost;
    }
    
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }
    
    public String getRemoteContext() {
        return remoteContext;
    }
    
    public void setRemoteContext(String remoteContext) {
        this.remoteContext = remoteContext;
    }
    
    public String getRemoteServiceName() {
        return remoteServiceName;
    }
    
    public void setRemoteServiceName(String remoteServiceName) {
        this.remoteServiceName = remoteServiceName;
    }
    //</editor-fold>
    
}
