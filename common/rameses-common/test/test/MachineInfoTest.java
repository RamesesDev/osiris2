/*
 * IOTest.java
 * JUnit based test
 *
 * Created on July 20, 2010, 7:28 AM
 */

package test;

import com.rameses.util.MachineInfo;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class MachineInfoTest extends TestCase {
    
    
    public MachineInfoTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void testMachineInfo() throws Exception {
        System.out.println( MachineInfo.getInstance().getMacAddress() );
    }
    
}

