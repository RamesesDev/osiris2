/*
 * XMPPConnectionManager.java
 *
 * Created on June 8, 2012, 5:53 PM
 * @author jaycverg
 */

package com.rameses.xmpp.service;

import com.rameses.server.common.JsonUtil;
import com.rameses.util.ExceptionManager;
import java.util.Hashtable;
import java.util.Map;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
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
    private String host;
    private int port;
    private String username;
    private String password;
    private ConnectionListener connListener;
    private boolean disconnected;
    
    
    public XMPPConnectionManager() {
    }
    
    public void connect(String host, int port, String username, String password) throws Exception {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        ConnectionConfiguration config = new ConnectionConfiguration(host, port);
        conn = new XMPPConnection(config);
        
        doConnect();
    }
    
    
    private void doConnect() {
        if( disconnected ) return;
        
        try {
            conn.connect();
            if( connListener == null ) {
                connListener = new Listener();
                conn.addConnectionListener(connListener);
            }
            try{
                conn.getAccountManager().createAccount(username, password);
            }catch(Exception ex){}

            if( listener != null ) {
                conn.addPacketListener(listener, listener);
            }

            conn.login(username, password, conn.getConnectionID());        
            conn.sendPacket(new Presence(Presence.Type.available, "Available", 1, Presence.Mode.available));
        }
        catch(Exception e) {
            System.out.println("Failed to connect: " + ExceptionManager.getOriginal(e).getMessage());
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("trying to reconnect in 5 seconds.");
                    try {
                        Thread.sleep(5000);
                        System.out.println("connecting...");
                        doConnect();
                    } 
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    
                }
            }).start();
        }
    }
    
    public void disconnect() throws Exception {
        disconnected = true;
        conn.disconnect();
    }
    
    public void disconnect(boolean removeAccount) throws Exception {
        disconnected = true;
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
    
    
    private class Listener implements ConnectionListener {
        
        public void connectionClosed() {
            System.out.println("connection closed.");
            System.out.println("retrying to connect...");
            doConnect();
        }
        public void connectionClosedOnError(Exception exception) {
            System.out.println("connection closed on error");
            System.out.println("retrying to connect...");
            doConnect();
        }
        public void reconnectingIn(int i) {
            System.out.println("reconnecting....");
        }
        public void reconnectionFailed(Exception exception) {
            System.out.println("reconnection failed.");
        }
        public void reconnectionSuccessful() {
            System.out.println("reconnected...");
        }
        
    }
}
