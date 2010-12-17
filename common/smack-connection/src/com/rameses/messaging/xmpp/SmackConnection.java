/*
 * SystemMessagingConnection.java
 *
 * Created on July 24, 2010, 9:54 AM
 * @author jaycverg
 */

package com.rameses.messaging.xmpp;

import com.rameses.messaging.*;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.PacketExtension;


public class SmackConnection extends MessagingConnection implements PacketListener, PacketFilter, ConnectionListener {
    
    public static final String NAME_SPACE = "smack:smackconnection";
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
        
        conn.addConnectionListener(this);
        conn.addPacketListener(this, this);
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
        for(String receiver: message.getReceivers()) {
            org.jivesoftware.smack.packet.Message smackMessage = new org.jivesoftware.smack.packet.Message();
            
            smackMessage.setFrom( message.getSender() );
            
            if(receiver.indexOf("@")<0) 
                receiver = receiver + "@" + getHost();

            smackMessage.setTo( receiver );
            smackMessage.setBody( message.getBody() );
            smackMessage.setSubject( message.getSubject() );
            if(  message.getChannel()!=null) {
                smackMessage.setThread( message.getChannel() );
            }
            PacketExtension pe = new DefaultPacketExtension(message.getType(), NAME_SPACE);
            smackMessage.addExtension(pe);
            conn.sendPacket( smackMessage );
        }
    }
    
    //filtering the message
    public boolean accept(Packet packet) {
        return packet.getExtension(NAME_SPACE) != null;
    }
    
    //receiving the message. implementation for PacketListener
    public void processPacket(Packet packet) {
        if( packet instanceof org.jivesoftware.smack.packet.Message ) {
            org.jivesoftware.smack.packet.Message smackMessage = (org.jivesoftware.smack.packet.Message) packet;
            PacketExtension pe = smackMessage.getExtension( NAME_SPACE );
            Message msg = rebuildMessage( pe.getElementName(), smackMessage );
            if(msg!=null) super.processMessage( msg );
        }
    }
    
    private Message rebuildMessage(String type, org.jivesoftware.smack.packet.Message pm ) {
        if(type.equals("system")) {
            SystemMessage sm = new SystemMessage(pm.getBody());
            sm.setSender( pm.getFrom() );
            sm.addReceiver( pm.getTo() );
            sm.setChannel( pm.getThread() );
            sm.setSubject( pm.getSubject() );
            return sm;
        }
        else {
            TextMessage tm = new TextMessage();
            tm.setBody( pm.getBody() );
            tm.setSender( pm.getFrom() );
            tm.addReceiver( pm.getTo() );
            tm.setChannel( pm.getThread() );
            tm.setSubject( pm.getSubject() );
            return tm;
        }
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
