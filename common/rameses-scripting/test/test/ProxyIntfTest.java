/*
 * ProxyIntfTest.java
 * JUnit based test
 *
 * Created on November 27, 2010, 9:05 AM
 */

package test;

import com.rameses.annotations.Async;
import com.rameses.annotations.ProxyMethod;
import com.rameses.scripting.InterfaceBuilder;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class ProxyIntfTest extends TestCase {
    
    public ProxyIntfTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        System.out.println( InterfaceBuilder.getProxyInterfaceScript("mypackage", Sample.class) );
        Method m = Sample.class.getDeclaredMethods()[0];
        System.out.println( InterfaceBuilder.createMethodSignature(m));
        
        Set s = new HashSet();
        s.add("cat");
        s.add("dog");
        s.add("cow");
        
        Set s1 = new HashSet();
        s1.add("dog");
        s1.add("cow");
        s1.add("cat");
        
        System.out.println(s1);
        System.out.println(s);
        
    }

    public static class Sample {
        @ProxyMethod
        @Async
        public String execute(String name) {
            return "hello";
        }
        
       
    }
    
    
}
