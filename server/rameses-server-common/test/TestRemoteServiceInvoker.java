import com.rameses.service.ServiceProxyAction;
import com.rameses.server.common.EJBServiceProxyRemote;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
/*
 * TestRemoteServiceInvoker.java
 * JUnit based test
 *
 * Created on September 17, 2011, 4:25 PM
 */

/**
 *
 * @author jzamss
 */
public class TestRemoteServiceInvoker extends TestCase {
    
    public TestRemoteServiceInvoker(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        Map conf = new HashMap();
        conf.put("app.context","gazeebu-classroom" );
        EJBServiceProxyRemote ri = new EJBServiceProxyRemote("localhost:8080", conf);
        ServiceProxyAction a = ri.create("TestSessionBean",new HashMap());
        System.out.println( a.invoke( "test", new Object[]{"fire test"} ) );
    }

}
