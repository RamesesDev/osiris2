
package com.rameses.xmpp.service;

import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import javax.naming.InitialContext;

public class XMPPService implements XMPPServiceMBean {

    protected String host;
    protected String port;
    protected String domain;
    protected String username;
    protected String password;
    
    
    public XMPPService() {
    }
    
    public void start() throws Exception {
        System.out.println("===========================================================");
        System.out.println("    STARTING XMPP SERVICE                                  ");
        System.out.println("===========================================================");
        
        XMPPConnectionManager.getInstance().connect(host, Integer.parseInt(port), username, password);        
        InitialContext ictx = new InitialContext();
        JndiUtil.bind( ictx, AppContext.getPath() + XMPPService.class.getSimpleName(), this );
    }
    
    public void stop() throws Exception {
        System.out.println("CLOSING XMPP SERVICE ");
        try {
            XMPPConnectionManager.getInstance().disconnect();
        }
        catch(Exception e){};
        
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind( ictx, AppContext.getPath() + XMPPService.class.getSimpleName() );
    }
    
    public void send(String username, Object message) throws Exception {
        if( !username.contains("@") ) {
            username = username + "@" + getDomain();
        }
        XMPPConnectionManager.getInstance().send(username, message);
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getHost() {
        return host;
    }
    
    public String getPort() {
        return port;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
}
