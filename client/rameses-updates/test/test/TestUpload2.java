/*
 * TestUpload.java
 * JUnit based test
 *
 * Created on September 15, 2009, 4:21 PM
 */

package test;

import com.rameses.client.updates.UpdateCenter;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class TestUpload2 extends TestCase {
    
    public TestUpload2(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        UpdateCenter c = new UpdateCenter( "file:///Users/elmo/updates/system/update.xml");
        c.start();
        
    }

}
