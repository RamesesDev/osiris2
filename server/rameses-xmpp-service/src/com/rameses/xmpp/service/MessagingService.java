
package com.rameses.xmpp.service;

import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;

public class MessagingService implements MessagingServiceMBean {
    
    private static final String PUB_CONN_KEY = "remote-public";
    private static final String PRIV_CONN_KEY = "remote-private";
    
    private String host;
    private String port;
    private String domain;
    
    private String originName;
    
    private String broadcastUsername;
    private String broadcastPassword;       
    private String privateUsername;
    private String privatePassword;
    
    
    public MessagingService() {
    }
    
    public void start() throws Exception {
        System.out.println("===========================================================");
        System.out.println("    STARTING MESSAGING SERVICE                    ");
        System.out.println("===========================================================");
        
        Map conf = new HashMap();
        try {
            String ctx = AppContext.getName();
            if(ctx!=null) conf.put("app.context",ctx);
        } catch(Exception e) {;}
        
        //-- public connection
        XMPPConnectionManager connMgr = XMPPConnectionManager.getInstance(PUB_CONN_KEY);
        connMgr.setListener(new ScriptMessageListener(conf, "RequestMessageHandler", getOriginName()));
        connMgr.connect(host, Integer.parseInt(port), broadcastUsername, broadcastPassword);
        
        //-- private connection
        connMgr = XMPPConnectionManager.getInstance(PRIV_CONN_KEY);
        connMgr.setListener(new ScriptMessageListener(conf, "ResponseMessageHandler", null));
        connMgr.connect(host, Integer.parseInt(port), privateUsername, privatePassword);
        
        InitialContext ictx = new InitialContext();
        JndiUtil.bind( ictx, AppContext.getPath() + MessagingService.class.getSimpleName(), this );
    }
    
    public void stop() throws Exception {
        System.out.println("CLOSING MESSAGING SERVICE ");
        //-- close public connection
        try {
            XMPPConnectionManager.getInstance(PUB_CONN_KEY).disconnect();
        }
        catch(Exception e){};
        
        //-- close private connection
        try {
            XMPPConnectionManager.getInstance(PRIV_CONN_KEY).disconnect(true);
        }
        catch(Exception e){};
        
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind( ictx, AppContext.getPath() + MessagingService.class.getSimpleName() );
    }
    
    public void send(String connection, String username, Object message) throws Exception {
        if( !username.contains("@") ) {
            username = username + "@" + getDomain();
        }
        
        XMPPConnectionManager.getInstance(connection).send(username, message);
    }

    public void broadcast(Object message) throws Exception {
        Map m = new HashMap();
        m.put("origin", getOriginName()); 
        m.put("responseUsername", getPrivateUsername()); //this will be used for the response
        m.put("requestId", "BRDCAST" + new java.rmi.server.UID());
        m.put("message", message);
        send( PUB_CONN_KEY, getBroadcastUsername(), m );
    }
    
    public void notify(Map request, Object message) throws Exception {
        Map m = new HashMap();
        m.put("origin", request.get("origin"));
        m.put("requestId", request.get("requestId"));
        m.put("message", message);
        send( PRIV_CONN_KEY, request.get("responseUsername")+"", m);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBroadcastUsername() {
        return broadcastUsername;
    }

    public void setBroadcastUsername(String broadcastUsername) {
        this.broadcastUsername = broadcastUsername;
    }

    public String getBroadcastPassword() {
        return broadcastPassword;
    }

    public void setBroadcastPassword(String broadcastPassword) {
        this.broadcastPassword = broadcastPassword;
    }

    public String getPrivateUsername() {
        return privateUsername;
    }

    public void setPrivateUsername(String privateUsername) {
        this.privateUsername = privateUsername;
    }

    public String getPrivatePassword() {
        return privatePassword;
    }

    public void setPrivatePassword(String privatePassword) {
        this.privatePassword = privatePassword;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
    //</editor-fold>
    
}
