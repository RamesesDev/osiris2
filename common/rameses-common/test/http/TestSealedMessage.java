package http;
import com.rameses.util.SealedMessage;
import junit.framework.*;
/*
 * HttpTest.java
 * JUnit based test
 *
 * Created on September 19, 2011, 10:00 AM
 */

/**
 *
 * @author jzamss
 */
public class TestSealedMessage extends TestCase {
    
    public TestSealedMessage(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testSimpleGet() throws Exception {
        String c = "cat";
        SealedMessage sm = new SealedMessage(c);
        System.out.println(sm.getMessage());
    }
   
}
