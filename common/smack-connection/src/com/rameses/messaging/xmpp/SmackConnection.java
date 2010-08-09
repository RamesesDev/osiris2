/*
 * SystemMessagingConnection.java
 *
 * Created on July 24, 2010, 9:54 AM
 * @author jaycverg
 */

package com.rameses.messaging.xmpp;

import com.rameses.messaging.*;
import java.util.Map;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.PacketExtension;


public class SmackConnection extends MessagingConnection implements PacketListener, PacketFilter, ConnectionListener {
    
    public static final String NAME_SPACE = "smack:asyncextension";
    private XMPPConnection conn;
    private boolean autoCreateAccount = true;
    private boolean connected;
    
    
    public SmackConnection() {
        setPort(5222);
    }
    
    public void open() throws Exception {
        ConnectionConfiguration conf = new ConnectionConfiguration(getHost(), getPort());
        conf.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        
        conn = new XMPPConnection(conf);
        conn.connect();
        conn.addConnectionListener(this);
        conn.addPacketListener(this, this);
        
        connected = true;
        super.notifyConnected();
        
        String username = getUsername();
        String password = getPassword();
        
        //force create the account.
        if ( autoCreateAccount ) {
            try {
                conn.getAccountManager().createAccount(username, password);
            } catch(Exception ign){;}
        }
        
        conn.login(username, password);
    }
    
    public void close() {
        if ( autoCreateAccount ) {
            try {
                conn.getAccountManager().deleteAccount();
            } catch(Exception ign){;}
        }
        conn.disconnect();
    }
    
    public void sendMessage(Message message) {
        Packet msg = (Packet) message.getMessage();
        PacketExtension pe = new DefaultPacketExtension(NAME_SPACE, NAME_SPACE);
        msg.addExtension(pe);
        conn.sendPacket( msg );
    }
    
    public Message createMessage(Map properties) {
        return new SmackMessage();
    }
    
    
    public void processPacket(Packet packet) {
        org.jivesoftware.smack.packet.Message sm = (org.jivesoftware.smack.packet.Message) packet;
        SmackMessage msg = new SmackMessage();
        msg.setMessage( sm );
        
        super.processMessage( msg );
    }
    
    public boolean accept(Packet packet) {
        return packet.getExtension(NAME_SPACE) != null;
    }
    
    //---- connection listening support
    public void connectionClosed() {
        connected = false;
        super.notifyDisconnected();
    }
    
    public void connectionClosedOnError(Exception e) {
        connected = false;
        super.notifyDisconnected();
    }
    
    public void reconnectionSuccessful() {
        connected = true;
        super.notifyConnected();
    }
    
    public void reconnectingIn(int i) {}
    public void reconnectionFailed(Exception exception) {}
    
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public boolean isAutoCreateAccount() {
        return autoCreateAccount;
    }
    
    public void setAutoCreateAccount(boolean autoCreateAccount) {
        this.autoCreateAccount = autoCreateAccount;
    }
    
    public boolean isConnected() {
        return connected;
    }
    //</editor-fold>
}
