/*
 * NotifierTest.java
 * JUnit based test
 *
 * Created on September 26, 2011, 4:08 PM
 */

package session;

import com.rameses.client.session.ConnectionListener;
import com.rameses.client.session.HttpNotificationServiceProvider;
import com.rameses.client.session.MessageListener;
import com.rameses.client.session.Notifier;
import com.rameses.service.EJBServiceContext;
import com.rameses.service.ServiceProxy;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class NotifierTest extends TestCase {
    
    public NotifierTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    
    private class MListener implements MessageListener {
        public void onMessage(Object data) {
            System.out.println("data received is " + data);
        }
    }
    
    private class ConnListener implements ConnectionListener {
        public void started() {
            System.out.println("started");
        }
        
        public void ended(String status) {
            System.out.println("ended->"+status);
        }
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestHello() throws Exception {
        String sessionid = "1K1JAK:674f9911:132a87ffd12:-7ff7";
        String host = "localhost:8080";
        String appContext = "gazeebu-classroom";
        HttpNotificationServiceProvider hs = new HttpNotificationServiceProvider(host,appContext);
        
        Notifier n = new Notifier(sessionid, hs);
        n.setConnectionListener(new ConnListener());
        n.addListener(new MListener());
        n.connect();
        
        JOptionPane.showMessageDialog(null, "blocking");
    }
    
    
    
    public void xtestGetInfo() throws Exception {
        Map conf = new HashMap();
        conf.put("app.context", "gazeebu-classroom");
        EJBServiceContext ctx = new EJBServiceContext(conf);
        ServiceProxy proxy = ctx.create("SessionService");
        Object info = proxy.invoke("getInfo", new Object[]{"1K1JAK:674f9911:132a87ffd12:-7ff4"} );
        System.out.println("info:" + info);
    }
    
    
     public void testSend() throws Exception {
        Map conf = new HashMap();
        conf.put("app.host", "10.0.0.118:8080");
        conf.put("app.context", "gazeebu-classroom");
        EJBServiceContext ctx = new EJBServiceContext(conf);
        ServiceProxy proxy = ctx.create("SessionService");
        proxy.invoke("push", new Object[]{"30RQM8:-6305317a:132a9239f2d:-7ffe", null, "hello"} );
    }
}
