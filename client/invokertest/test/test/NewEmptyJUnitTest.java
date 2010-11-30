/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on October 13, 2010, 4:39 PM
 */

package test;

import com.rameses.common.AsyncHandler;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import junit.framework.*;
import org.apache.commons.beanutils.MethodUtils;

import tester.TestProxy;

/**
 *
 * @author ms
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestHello2() throws Exception {
        Map env = new HashMap();
        env.put( "default.host", "10.0.0.118:8080" );
        env.put( "app.context", "mlglobal" );
        env.put( "session_checked", true );
        TestProxy p = new TestProxy(env);
        Object o = p.create("TestService");
        System.out.println( MethodUtils.invokeMethod( o, "test", new Object[]{"elmo"} ) );
    }
    
    public void testAsync1() throws Exception {
        Map env = new HashMap();
        env.put( "default.host", "10.0.0.118:8080" );
        env.put( "app.context", "mlglobal" );
        env.put( "session_checked", true );
        TestProxy p = new TestProxy(env);
        Object o = p.create("TestService");
        System.out.println( MethodUtils.invokeMethod( o, "testAsync", new Object[]{"elmo", new MyHandler()} ) );
        JOptionPane.showMessageDialog(null, "pause");
    }

    public class MyHandler implements AsyncHandler {
        public void onMessage(Object o) {
            System.out.println("receiving message ->" + o );
        }
    }
    
}
