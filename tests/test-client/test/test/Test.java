package test;
import javax.swing.JDialog;
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
        JDialog d = new JDialog();
        d.setContentPane(new TestPage());
        d.setModal(true);
        d.pack();
        d.setVisible(true);
    }
    
}
