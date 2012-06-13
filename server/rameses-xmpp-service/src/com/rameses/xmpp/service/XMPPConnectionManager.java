/*
 * XMPPConnectionManager.java
 *
 * Created on June 8, 2012, 5:53 PM
 * @author jaycverg
 */

package com.rameses.xmpp.service;

import com.rameses.server.common.JsonUtil;
import java.util.Hashtable;
import java.util.Map;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;


public class XMPPConnectionManager {
    
    private static Map<String, XMPPConnectionManager> instances = new Hashtable();
    
    public static XMPPConnectionManager getInstance() {
        return getInstance("DEFAULT");
    }
    
    public static XMPPConnectionManager getInstance(String name) {
        XMPPConnectionManager instance = instances.get(name);
        if( instance != null ) return instance;
        
        instances.put(name, (instance = new XMPPConnectionManager()));
        return instance;
    }
    
    
    //local xmpp account
    private XMPPConnection conn;
    private ScriptMessageListener listener;
    
    public XMPPConnectionManager() {
    }
    
    public void connect(String host, int port, String username, String password) throws Exception {
        ConnectionConfiguration config = new ConnectionConfiguration(host, port);
        conn = new XMPPConnection(config);
        conn.connect();
        try{
            conn.getAccountManager().createAccount(username, password);
        }catch(Exception ex){}
        
        if( listener != null ) {
            conn.addPacketListener(listener, listener);
        }
        
        conn.login(username, password, conn.getConnectionID());        
        conn.sendPacket(new Presence(Presence.Type.available, "Available", 1, Presence.Mode.available));
    }
    
    public void disconnect() throws Exception {
        conn.disconnect();
    }
    
    public void disconnect(boolean removeAccount) throws Exception {
        if( removeAccount ) {
            try {
                conn.getAccountManager().deleteAccount();
            }
            catch(Exception e){}
        }
        conn.disconnect();
    }
    
    public void send(String username, Object message) {
        try {
            Message msg = new Message();
            msg.setType(Message.Type.chat);
            msg.setBody(JsonUtil.toString(message));
            msg.setTo(username);
            conn.sendPacket(msg);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ScriptMessageListener getListener() {
        return listener;
    }

    public void setListener(ScriptMessageListener listener) {
        this.listener = listener;
    }
}
