/*
 * Main.java
 *
 * Created on July 24, 2010, 11:28 AM
 * @author jaycverg
 */

package test.smack;

import com.rameses.messaging.MessageListener;
import com.rameses.messaging.SmackMessage;
import com.rameses.messaging.SmackMessagingConnection;
import javax.swing.JOptionPane;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;


public class Main {
    
    public static void main(String[] args) throws Exception {
        SmackMessagingConnection con = SmackDelegate.getConnection("paadmin", "12345");
        con.open();
        JOptionPane.showMessageDialog(null, "logging in as paadmin...");
        
        SmackMessagingConnection con2 = SmackDelegate.getConnection("jay", "12345");
        con2.open();
        JOptionPane.showMessageDialog(null, "logging in as jay...");
        
        SmackMessage m = (SmackMessage) con.createMessage(null);
        m.setText("hello from paaadmin");
        m.setRecepient("jay@etracs.org");
        
        con.sendMessage(m);
        
        con.addListener(new MessageListener() {
            public void onMessage(Object message) {
                Packet packet = (Packet) message;
                System.out.println("from: " + packet.getFrom());
                System.out.println("to: " + packet.getTo());
                System.out.println("message: " + ((Message) packet).getBody());
                System.out.println("==================================================");
            }
        });
        
        SmackMessage m2 = (SmackMessage) con2.createMessage(null);
        m2.setText("hello from jay");
        m2.setRecepient("paadmin@etracs.org");
        
        con2.sendMessage(m2);
        
        con2.addListener(new MessageListener() {
            public void onMessage(Object message) {
                Packet packet = (Packet) message;
                System.out.println("from: " + packet.getFrom());
                System.out.println("to: " + packet.getTo());
                System.out.println("message: " + ((Message) packet).getBody());
                System.out.println("==================================================");
            }
        });
        
        JOptionPane.showMessageDialog(null, "closing...");
        con.close();
        con2.close();
    }
    
}
