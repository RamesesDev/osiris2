import javax.swing.UIManager;
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
    
    private String[] args;
    
    public void testHello() {
        for ( String s: args ) {
            System.out.println( s );
        }
    }
    
}
