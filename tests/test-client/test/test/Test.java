package test;
import java.text.ParseException;
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
    
//    public void test() throws ParseException {
//        System.out.println("#eef433".matches("#[a-f\\d]{3,6}") );
//    }
    
    public void test2() throws ParseException {
        JDialog d = new JDialog();
        d.setModal(true);
        d.setContentPane(new TestPage2());
        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }
    
}
