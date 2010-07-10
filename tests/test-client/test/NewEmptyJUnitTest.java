import javax.swing.JDialog;
import junit.framework.*;
import test.client2.TestPage3;

/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on June 24, 2010, 1:13 PM
 */

/**
 *
 * @author compaq
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }
    
    public void testHello() throws Exception 
    {
//        try {
//            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
//        } catch(Exception e) {;}
        
        JDialog d = new JDialog();
        d.setModal(true);
        d.setContentPane(new TestPage3());
        d.setSize(500, 500);
        //d.setVisible(true);

    }

}
