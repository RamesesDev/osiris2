package test;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    
    public void test() throws ParseException {
        Matcher m = Pattern.compile("([\\s\\w]+)\\s+(\\w+)\\s+(\\d+)$").matcher("Times New Roman bold 12");
        if ( m.matches() ) {
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            System.out.println(m.group(3));
        }
    }
    
//    public void test2() throws ParseException {
//        JDialog d = new JDialog();
//        d.setModal(true);
//        d.setContentPane(new TestPage());
//        d.pack();
//        d.setLocationRelativeTo(null);
//        d.setVisible(true);
//    }
    
}
