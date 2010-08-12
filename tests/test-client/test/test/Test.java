package test;
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
    
    public void testHello() throws Exception {
        String s = "java.lang.Integer";
        System.out.println(s.replaceAll("\\.", "/"));
    }
    
}
