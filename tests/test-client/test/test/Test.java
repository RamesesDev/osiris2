package test;
import com.rameses.util.MachineInfo;
import junit.framework.*;

/*
 * Test.java
 * JUnit based test
 *
 * Created on June 22, 2010, 4:17 PM
 */

/**
 *
 * @author compaq
 */
public class Test extends TestCase {
    
    public Test(String testName) {
        super(testName);
    }
    
    public void testHello() throws Exception {
        String mcaddress = MachineInfo.getInstance().getMacAddress();
        System.out.println( mcaddress );
        System.out.println( mcaddress.hashCode() );
        System.out.println( (mcaddress+"1").hashCode() );
        
    }
    
}
