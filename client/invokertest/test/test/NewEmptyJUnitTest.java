/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on October 13, 2010, 4:39 PM
 */

package test;

import com.rameses.common.AsyncListener;
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
    public void testHello2() throws Exception {
        Map env = new HashMap();
        env.put( "default.host", "10.0.0.118:8080" );
        env.put( "app.context", "mlglobal" );
        TestProxy p = new TestProxy(env);
        Object o = p.create("TestService");
        Map n = new HashMap();
        n.put("name", "elmo");
        System.out.println( MethodUtils.invokeMethod( o, "test", new Object[]{n} ) );
    }
    
    public void testAsync1() throws Exception {
        Map env = new HashMap();
        env.put( "default.host", "10.0.0.118:8080" );
        env.put( "app.context", "mlglobal" );
        TestProxy p = new TestProxy(env);
        
        String n = "elmo";
        Map map = new HashMap();
        map.put("fire", new MyHandler() );
        Object o = p.create("AsyncTestService", map);
        
        System.out.println( MethodUtils.invokeMethod( o, "fire", new Object[]{n} ) );
        JOptionPane.showMessageDialog(null, "pause");
    }
    
    public class MyHandler implements AsyncListener {
        public void onMessage(Object o) {
            System.out.println("receiving message ->" + o );
        }
    }
    
}
