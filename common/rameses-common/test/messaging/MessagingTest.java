/*
 * MessagingTest.java
 * JUnit based test
 *
 * Created on July 27, 2011, 12:07 PM
 */

package messaging;

import com.rameses.invoker.client.SimpleHttpClient;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class MessagingTest extends TestCase {
    
    public MessagingTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testSend() throws Exception {
        SimpleHttpClient s = new SimpleHttpClient("http://localhost:8080/messaging-server");
        Map map = new HashMap();
        map.put("message", "name=Ban&age=30");
        System.out.println("result ->" + s.post("send", map) );
    }

}
