import com.rameses.util.MachineInfo;
import junit.framework.*;

/*
 * Test1.java
 * JUnit based test
 *
 * Created on May 16, 2011, 2:54 PM
 * @author jaycverg
 */

public class Test1 extends TestCase {
    
    public Test1(String testName) {
        super(testName);
    }
    
    public void testHello() throws Exception {
      System.out.println( MachineInfo.getInstance().getMacAddress() );
    }    
    
}
