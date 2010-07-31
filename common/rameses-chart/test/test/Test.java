/*
 * Test.java
 * JUnit based test
 *
 * Created on June 17, 2009, 3:16 PM
 */

package test;

import java.util.Date;
import junit.framework.*;

/**
 *
 * @author rameses
 */
public class Test extends TestCase {
    
    public Test(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    public void test() {
        System.out.println( new Date("2010/01/02") );
    }

}
