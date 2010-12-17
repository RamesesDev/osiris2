import com.rameses.messaging.xmpp.SmackConnection;
import junit.framework.*;

/*
 * Test1.java
 * JUnit based test
 *
 * Created on August 9, 2010, 11:38 AM
 * @author jaycverg
 */

public class UsageTest extends TestCase {
    
    public void testHello() throws Exception {
        SmackConnection c = new SmackConnection();
        c.setUsername( "a" );
        c.setPassword( "pwd"  );
        c.setHost( "10.0.0.104");
        c.setPort(5222);
        c.open();
    }
    
}
