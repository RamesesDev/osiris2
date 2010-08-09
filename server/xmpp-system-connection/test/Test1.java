import junit.framework.*;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/*
 * Test1.java
 * JUnit based test
 *
 * Created on August 9, 2010, 11:38 AM
 * @author jaycverg
 */

public class Test1 extends TestCase {
    
    public void testHello() {
        ConnectionConfiguration conf = new ConnectionConfiguration("etracs.org", 5222);
        conf.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        
        XMPPConnection conn = null;
        try {
            conn = new XMPPConnection(conf);
            conn.connect();
            //conn.addPacketListener( this, this );
            conn.login("jay", "123457");
        } catch(XMPPException e) {
            System.out.println( e.getXMPPError() );
            e.printStackTrace();
        }
    }
    
}
