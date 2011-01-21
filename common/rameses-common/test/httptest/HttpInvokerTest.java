/*
 * HttpInvokerTest.java
 * JUnit based test
 *
 * Created on January 21, 2011, 8:46 AM
 */

package httptest;

import com.rameses.invoker.client.DynamicHttpInvoker;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class HttpInvokerTest extends TestCase {
    
    public HttpInvokerTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        DynamicHttpInvoker h = new DynamicHttpInvoker("localhost:8080", "mlglobal");
        DynamicHttpInvoker.Action a = h.create("DateService");
        System.out.println( a.invoke( "getServerDate") );
    
    }

}
