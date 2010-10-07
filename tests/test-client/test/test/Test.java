package test;
import java.text.ParseException;
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
        for(String s : "click?hello world".split("\\?") ) {
            System.out.println(s);
        }
    }
    
}
