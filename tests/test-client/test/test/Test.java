package test;
import javax.swing.JTree;
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
    
    public void testHello() {
        JTree t = new JTree();
        for ( Object o: t.getActionMap().allKeys() ) {
            System.out.println( o );
        }
    }
    
}
