/*
 * ScriptDelegateTest.java
 * JUnit based test
 *
 * Created on October 24, 2010, 1:14 PM
 */

package httptest;

import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import com.rameses.invoker.client.HttpScriptService;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class ScriptDelegateTest extends TestCase {
    
    public ScriptDelegateTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testSimpleInvoke1() throws Exception {
        Map env = new HashMap();
        env.put("default.host", "10.0.0.118:8080");
        env.put("app.context", "mlglobal");
        env.put("session_checked", true);
        HttpInvokerClient c = HttpClientManager.getInstance() .getService( env );
        HttpScriptService d = new HttpScriptService(c);
        System.out.println( d.invoke("TestService", "test", new Object[]{"elmo"},env) );
    }

   
    
    
}
