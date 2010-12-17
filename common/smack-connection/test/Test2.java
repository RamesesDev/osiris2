import com.rameses.messaging.ConnectionListener;
import com.rameses.messaging.Message;
import com.rameses.messaging.MessageListener;
import com.rameses.messaging.TextMessage;
import com.rameses.messaging.xmpp.SmackConnection;
import javax.swing.JOptionPane;
import junit.framework.*;
/*
 * Test2.java
 * JUnit based test
 *
 * Created on December 17, 2010, 9:54 AM
 * @author jaycverg
 */

public class Test2 extends TestCase {
    
    private SmackConnection con;
    
    public Test2(String testName) {
        super(testName);
    }
    
    public void testHello() throws Exception {
        con = new SmackConnection();
        con.setAutoCreateAccount(false);
        con.setHost("etracs.org");
        con.setPort(5222);
        con.setUsername("admin");
        con.setPassword("admin1234");
        con.addMessageListener( new Listener() );
        
        con.open();
        
        TextMessage m = new TextMessage();
        m.setBody("hello world.");
        m.setSubject("Test message");
        m.addReceiver("admin@etracs.org");
        
        con.sendMessage( m );
        JOptionPane.showMessageDialog(null, "wainting...");
    }
    
    private class Listener implements MessageListener {
        
        public void onMessage(Message message) {
            System.out.println("message is " + message);
            con.close();
        }
        
    }
    
    private class ConListener implements ConnectionListener {
        
        public void onConnect() {
        }

        public void onDisconnect() {
        }
        
    }

}
