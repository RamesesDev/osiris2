/*
 * Test2.java
 * JUnit based test
 *
 * Created on November 8, 2010, 2:38 PM
 */

package test;

import com.rameses.persistence.EntityManager;
import com.rameses.rcp.framework.Binding;
import junit.framework.*;

/**
 *
 * @author rameses
 */
public class Test2 extends TestCase {
    
    
    public Test2(String testName) {
        super(testName);
    }
    
    public void testHello() {
//        EntityManager em;
//        Binding b;
        InvokerUtil.lookup("", null);
    }
    
}
