/*
 * SmackMessagingConnection.java
 *
 * Created on July 24, 2010, 9:54 AM
 * @author jaycverg
 */

package com.rameses.messaging;

import java.util.Map;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;


public class SmackMessagingConnection extends MessagingConnection implements PacketListener, PacketFilter {
    
    private XMPPConnection conn;
    
    public SmackMessagingConnection() {
        setPort(5222);
    }
    
    public void open() throws Exception {
        ConnectionConfiguration conf = new ConnectionConfiguration(getHost(), getPort());
        conf.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        
        conn = new XMPPConnection(conf);
        conn.connect();
        conn.addPacketListener( this, this );
        conn.login(getUsername(), getPassword());
    }
    
    public void close() {
        conn.disconnect();
    }
    
    public void sendMessage(Message message) {
        conn.sendPacket((Packet) message.getMessage());
    }
    
    public Message createMessage(Map properties) {
        return new SmackMessage();
    }
    
    
    public void processPacket(Packet packet) {
        super.processMessage(packet);
    }
    
    public boolean accept(Packet packet) {
        return true;
    }
    
}
