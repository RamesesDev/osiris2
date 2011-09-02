/*
 * TestSendMessage.java
 * JUnit based test
 *
 * Created on August 1, 2011, 2:43 PM
 */

package test;

import com.rameses.invoker.client.SimpleHttpClient;
import com.rameses.invoker.client.SimpleHttpPoller;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class TestSendMessage extends TestCase {
    
    public TestSendMessage(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        SimpleHttpClient c = new SimpleHttpClient("http://10.0.0.118:8080/upcebu-gazeebu");
        Map data = new HashMap();
        data.put("message", "hello friend 9" );
        data.put("id", "worgie" );
        c.post("signal", data);
        data = new HashMap();
        data.put("message", "hello friend 10" );
        data.put("id", "worgie" );
        c.post("signal", data);
    }
    
    private static class MyHandler implements SimpleHttpPoller.MessageHandler {
        public void onMessage(String result) {
            System.out.println("result is ->"+result);
        }
    }
    
    public void xtestPoll() throws Exception {
        System.out.println("start poller");
        SimpleHttpPoller poller = new SimpleHttpPoller("http://10.0.0.118:8080/upcebu-gazeebu", "worgie");
        poller.addHandler( new MyHandler());
        poller.start();
    }
    
    public void xtestListen() throws Exception {
        System.out.println("start listening");
        SimpleHttpClient c = new SimpleHttpClient("http://localhost:8080/upcebu-gazeebu");
        Map data = new HashMap();
        data.put("id", "worgie" );
        JOptionPane.showMessageDialog(null, c.post("poll",data));
    }
    
}
