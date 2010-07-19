import javax.swing.JDialog;
import junit.framework.*;
import test.TestPage;

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
    
    public void testHello() {        
        System.out.println( "sample-one:workunit2".matches(".+:.+") );
    }
    
}
