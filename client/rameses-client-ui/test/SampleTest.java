import junit.framework.*;
/*
 * SampleTest.java
 * JUnit based test
 *
 * Created on January 25, 2011, 7:34 AM
 */

/**
 *
 * @author ms
 */
public class SampleTest extends TestCase {
    
    public SampleTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        String c = "customer";
        String b = "customer.sender.firstname";
        
        System.out.println(b.replaceAll( c+"\\.", ""));
    }

}
